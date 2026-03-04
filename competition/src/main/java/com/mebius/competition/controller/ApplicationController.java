package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.Application;
import com.mebius.competition.entity.Competition;
import com.mebius.competition.entity.Team;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.ApplicationMapper;
import com.mebius.competition.mapper.CompetitionMapper;
import com.mebius.competition.mapper.TeamMapper;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/application")
@CrossOrigin
public class ApplicationController {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private CompetitionMapper competitionMapper;

    @Autowired
    private UserMapper userMapper;

    //
    @Autowired
    private TeamMapper teamMapper;
    @PostMapping("/submit")
    public Map<String, Object> submitApplication(@RequestBody Application application) {
        Map<String, Object> result = new HashMap<>();
        application.setSubmitTime(LocalDateTime.now());
        application.setStatus("pending");
        applicationMapper.insert(application);
        result.put("success", true);
        result.put("message", "报名提交成功！");
        return result;
    }

    @GetMapping("/my")
    public List<Map<String, Object>> getMyApplications(@RequestParam String studentId) {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId).orderByDesc("submit_time");
        List<Application> apps = applicationMapper.selectList(queryWrapper);
        return packageApplicationList(apps);
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getAllApplications() {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("submit_time");
        List<Application> apps = applicationMapper.selectList(queryWrapper);
        return packageApplicationList(apps);
    }

    @PostMapping("/review")
    public Map<String, Object> reviewApplication(@RequestBody Map<String, Object> reviewData) {
        Map<String, Object> result = new HashMap<>();

        // 提取 ID (现在前端发来的是 String 格式了，依然可以用 Long.parseLong 解析)
        Long appId = Long.parseLong(reviewData.get("id").toString());
        String status = reviewData.get("status").toString();
        String feedback = reviewData.get("feedback") != null ? reviewData.get("feedback").toString() : "";
        Integer score = reviewData.get("score") != null && !reviewData.get("score").toString().isEmpty()
                ? Integer.parseInt(reviewData.get("score").toString()) : null;

        Application app = applicationMapper.selectById(appId);
        if (app != null) {
            app.setStatus(status);
            app.setFeedback(feedback);
            app.setScore(score);
            applicationMapper.updateById(app);

            result.put("success", true);
            result.put("message", "审核保存成功！");
        } else {
            result.put("success", false);
            result.put("message", "找不到该报名记录！");
        }
        return result;
    }

    @PostMapping("/bulkReview")
    public Map<String, Object> bulkReview(@RequestBody Map<String, Object> bulkData) {
        Map<String, Object> result = new HashMap<>();

        // 【重要修复】因为 ID 变成了字符串，所以这里的接收类型也要改成 List<String> 才能防报错
        List<String> idsStr = (List<String>) bulkData.get("ids");
        String status = bulkData.get("status").toString();
        String defaultFeedback = "approved".equals(status) ? "条件符合，批量通过。" : "未达到要求，予以驳回。";

        for (String idStr : idsStr) {
            Application app = applicationMapper.selectById(Long.parseLong(idStr));
            if (app != null) {
                app.setStatus(status);
                app.setFeedback(defaultFeedback);
                applicationMapper.updateById(app);
            }
        }

        result.put("success", true);
        result.put("message", "批量操作完成！");
        return result;
    }

    /**
     * 私有辅助工具：把死板的数据表打包成带有详细中文名称的“大纸箱”
     */
    private List<Map<String, Object>> packageApplicationList(List<Application> apps) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Application app : apps) {
            Map<String, Object> map = new HashMap<>();

            map.put("id", String.valueOf(app.getId()));
            map.put("statement", app.getStatement());
            map.put("status", app.getStatus());
            map.put("score", app.getScore());
            map.put("feedback", app.getFeedback());
            map.put("submitTime", app.getSubmitTime());
            // 🌟 新增修复：把“提货单”(文件下载链接) 也打包发给前端的老师！
            map.put("fileUrl", app.getFileUrl());

            User student = userMapper.selectById(app.getStudentId());
            map.put("studentName", student != null ? student.getName() : "未知学生");

            Competition comp = competitionMapper.selectById(app.getCompId());
            map.put("compTitle", comp != null ? comp.getTitle() : "未知竞赛");

            // 🌟 核心：如果是队伍报名，查出队伍名称
            if (app.getTeamId() != null) {
                Team team = teamMapper.selectById(app.getTeamId());
                map.put("teamName", team != null ? team.getName() : "未知队伍");
            } else {
                map.put("teamName", null); // 代表个人报名
            }

            resultList.add(map);
        }
        return resultList;
    }
}