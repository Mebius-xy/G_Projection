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

    @Autowired
    private TeamMapper teamMapper;

    /**
     * 1. 提交报名申请
     */
    @PostMapping("/submit")
    public Map<String, Object> submitApplication(@RequestBody Application application) {
        Map<String, Object> result = new HashMap<>();

        // 🌟 防呆设计：检查是否已经报名过这个比赛，防止手抖重复提交
        Long count = applicationMapper.selectCount(new QueryWrapper<Application>()
                .eq("student_id", application.getStudentId())
                .eq("comp_id", application.getCompId()));
        if (count > 0) {
            result.put("success", false);
            result.put("message", "您已经报名过该竞赛，请勿重复提交！");
            return result;
        }

        application.setSubmitTime(LocalDateTime.now());

        // 🌟 核心业务升级：VIP 免检通道判断 🌟
        Competition comp = competitionMapper.selectById(application.getCompId());
        if (comp != null && comp.getNeedReview() != null && comp.getNeedReview() == 0) {
            // 如果竞赛设置了 0 (免审核通配模式)，直接盖章通过！
            application.setStatus("approved");
        } else {
            // 否则老老实实排队等老师审核
            application.setStatus("pending");
        }

        applicationMapper.insert(application);

        result.put("success", true);
        result.put("message", "操作成功！");
        return result;
    }

    /**
     * 2. 学生获取自己的报名记录
     */
    @GetMapping("/my")
    public List<Map<String, Object>> getMyApplications(@RequestParam String studentId) {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId).orderByDesc("submit_time");
        List<Application> apps = applicationMapper.selectList(queryWrapper);
        return packageApplicationList(apps);
    }

    /**
     * 3. 教师获取全校的报名记录
     */
    @GetMapping("/all")
    public List<Map<String, Object>> getAllApplications() {
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("submit_time");
        List<Application> apps = applicationMapper.selectList(queryWrapper);
        return packageApplicationList(apps);
    }

    /**
     * 4. 教师单条审核与打分
     */
    @PostMapping("/review")
    public Map<String, Object> reviewApplication(@RequestBody Map<String, Object> reviewData) {
        Map<String, Object> result = new HashMap<>();

        // 提取 ID (现在前端发来的是 String 格式了，用 Long.parseLong 解析)
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

    /**
     * 5. 教师批量审核
     */
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

            // 【🌟 核心修复点 1 🌟】强制将雪花 ID 转为字符串，防前端精度丢失
            map.put("id", String.valueOf(app.getId()));
            // 【🌟 核心修复点 2 🌟】必须把 compId 放进去并转成字符串，大厅卡片才能认出它！
            map.put("compId", String.valueOf(app.getCompId()));

            map.put("statement", app.getStatement());
            map.put("status", app.getStatus());
            map.put("score", app.getScore());
            map.put("feedback", app.getFeedback());
            map.put("submitTime", app.getSubmitTime());

            // 带上真实文件的下载链接
            map.put("fileUrl", app.getFileUrl());

            // 翻译学生姓名
            User student = userMapper.selectById(app.getStudentId());
            map.put("studentName", student != null ? student.getName() : "未知学生");

            // 翻译竞赛名称
            Competition comp = competitionMapper.selectById(app.getCompId());
            map.put("compTitle", comp != null ? comp.getTitle() : "未知竞赛");

            // 如果填了队伍ID，顺便把队伍名字翻译出来
            if (app.getTeamId() != null) {
                Team team = teamMapper.selectById(app.getTeamId());
                map.put("teamName", team != null ? team.getName() : "神秘车队");
            }

            resultList.add(map);
        }
        return resultList;
    }
}