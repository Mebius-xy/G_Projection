package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 1. 修改密码接口
     */
    @PostMapping("/updatePassword")
    public Map<String, Object> updatePassword(@RequestBody Map<String, String> requestData) {
        Map<String, Object> result = new HashMap<>();
        String userId = requestData.get("userId");
        String newPassword = requestData.get("newPassword");

        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setPassword(newPassword);
            user.setIsFirstLogin(false);
            userMapper.updateById(user);
            result.put("success", true);
            result.put("message", "密码修改成功！");
        } else {
            result.put("success", false);
            result.put("message", "用户不存在！");
        }
        return result;
    }

    /**
     * 2. 获取指定班级的所有学生 (教师权限用)
     */
    @GetMapping("/byClass")
    public List<User> getStudentsByClass(@RequestParam String className) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("role", "student").eq("class_name", className);

        List<User> students = userMapper.selectList(query);
        for(User s : students) {
            s.setPassword(null); // 脱敏，防止密码泄露
        }
        return students;
    }

    /**
     * 3. 教师修改学生的竞赛基础分
     */
    @PostMapping("/updateScore")
    public Map<String, Object> updateScore(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        String studentId = data.get("studentId").toString();
        Integer newScore = Integer.parseInt(data.get("score").toString());

        User student = userMapper.selectById(studentId);
        if (student != null) {
            student.setBaseScore(newScore);
            userMapper.updateById(student);
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }

    /**
     * 4. 获取全局用户信息 (支持分页和按角色过滤)
     */
    @GetMapping("/page")
    public Map<String, Object> getUsersByPage(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String role) {

        Map<String, Object> result = new HashMap<>();

        // 准备一个分页专用的空壳子
        Page<User> pageInfo = new Page<>(currentPage, pageSize);

        // 准备查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (role != null && !role.isEmpty()) {
            queryWrapper.eq("role", role);
        }

        // 查数据
        Page<User> userPage = userMapper.selectPage(pageInfo, queryWrapper);

        // 数据脱敏
        List<User> records = userPage.getRecords();
        for(User u : records) {
            u.setPassword(null);
        }

        result.put("records", records);
        result.put("total", userPage.getTotal());
        result.put("pages", userPage.getPages());

        return result;
    }

    /**
     * 5. 修改个人资料 (姓名、个性签名)
     */
    @PostMapping("/updateProfile")
    public Map<String, Object> updateProfile(@RequestBody User userUpdates) {
        Map<String, Object> result = new HashMap<>();

        User user = userMapper.selectById(userUpdates.getId());
        if (user != null) {
            // 只更新允许修改的字段
            user.setName(userUpdates.getName());
            user.setSignature(userUpdates.getSignature());

            userMapper.updateById(user);

            // 安全处理，把密码抹掉再返回给前端
            user.setPassword(null);

            result.put("success", true);
            result.put("message", "资料修改成功！");
            result.put("data", user); // 返回最新的用户信息供前端更新本地存储
        } else {
            result.put("success", false);
            result.put("message", "找不到该用户！");
        }
        return result;
    }
}