package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.PermissionApproval;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.PermissionApprovalMapper;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permission")
@CrossOrigin
public class PermissionController {

    @Autowired
    private PermissionApprovalMapper permissionMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 1. 教师提交申请
     */
    @PostMapping("/submit")
    public Map<String, Object> submitApplication(@RequestBody PermissionApproval approval) {
        Map<String, Object> result = new HashMap<>();
        approval.setApplyTime(LocalDateTime.now());
        approval.setStatus("pending");
        permissionMapper.insert(approval);

        result.put("success", true);
        result.put("message", "权限申请已提交，请等待管理员审批！");
        return result;
    }

    /**
     * 2. 教师查询自己的所有申请记录
     */
    @GetMapping("/my")
    public List<Map<String, Object>> getMyPermissions(@RequestParam String teacherId) {
        QueryWrapper<PermissionApproval> query = new QueryWrapper<>();
        query.eq("teacher_id", teacherId).orderByDesc("apply_time");
        List<PermissionApproval> list = permissionMapper.selectList(query);

        // 打包时顺便把教师的真实姓名查出来放进去
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (PermissionApproval p : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(p.getId())); // 防精度丢失
            map.put("targetClass", p.getTargetClass());
            map.put("reason", p.getReason());
            map.put("status", p.getStatus());
            map.put("applyTime", p.getApplyTime());

            User teacher = userMapper.selectById(p.getTeacherId());
            map.put("teacherName", teacher != null ? teacher.getName() : "未知教师");
            resultList.add(map);
        }
        return resultList;
    }
    /**
     * 3. 管理员获取【所有】权限申请记录
     */
    @GetMapping("/all")
    public List<Map<String, Object>> getAllPermissions() {
        // 查出所有人提交的申请
        List<PermissionApproval> list = permissionMapper.selectList(new QueryWrapper<PermissionApproval>().orderByDesc("apply_time"));

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (PermissionApproval p : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(p.getId()));
            map.put("targetClass", p.getTargetClass());
            map.put("reason", p.getReason());
            map.put("status", p.getStatus());
            map.put("applyTime", p.getApplyTime());

            User teacher = userMapper.selectById(p.getTeacherId());
            map.put("teacherName", teacher != null ? teacher.getName() : "未知教师");
            resultList.add(map);
        }
        return resultList;
    }

    /**
     * 4. 管理员处理审批 (盖章：批准/驳回)
     */
    @PostMapping("/process")
    public Map<String, Object> processPermission(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        Long id = Long.parseLong(data.get("id").toString());
        String status = data.get("status").toString();

        PermissionApproval approval = permissionMapper.selectById(id);
        if (approval != null) {
            approval.setStatus(status); // 盖章：approved 或 rejected
            permissionMapper.updateById(approval);
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }
}