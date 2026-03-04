package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// ================== 🌟 核心更新 1：新增导出 Excel 必备的包 ==================
import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
// =========================================================================

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
     * 1. 修改密码接口 (首次登录强制改密)
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
     * 2. 获取指定班级的所有学生 (教师一表多用权限)
     */
    @GetMapping("/byClass")
    public List<User> getStudentsByClass(@RequestParam String className) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("role", "student").eq("class_name", className);

        List<User> students = userMapper.selectList(query);
        for(User s : students) {
            s.setPassword(null); // 脱敏保护
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
     * 4. 获取全局用户信息 (管理员分页与检索库)
     */
    @GetMapping("/page")
    public Map<String, Object> getUsersByPage(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role) {

        Map<String, Object> result = new HashMap<>();
        Page<User> pageInfo = new Page<>(currentPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 如果传了角色分类，就加上角色过滤
        if (role != null && !role.isEmpty()) {
            queryWrapper.eq("role", role);
        }
        // 如果传了搜索关键词，就模糊搜索姓名、学号或班级
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(w -> w.like("name", keyword)
                    .or().like("id", keyword)
                    .or().like("class_name", keyword));
        }

        Page<User> userPage = userMapper.selectPage(pageInfo, queryWrapper);

        List<User> records = userPage.getRecords();
        for(User u : records) {
            u.setPassword(null); // 列表数据绝对不能带密码
        }

        result.put("records", records);
        result.put("total", userPage.getTotal());
        result.put("pages", userPage.getPages());

        return result;
    }

    /**
     * 5. 公共主页：获取他人的公开信息
     */
    @GetMapping("/publicInfo/{id}")
    public Map<String, Object> getPublicInfo(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(null); // 脱敏
            result.put("success", true);
            result.put("data", user);
        } else {
            result.put("success", false);
        }
        return result;
    }

    /**
     * 6. 个人中心：更新个人资料 (姓名、签名等)
     */
    @PostMapping("/updateProfile")
    public Map<String, Object> updateProfile(@RequestBody Map<String, String> data) {
        Map<String, Object> result = new HashMap<>();
        String id = data.get("id");

        User user = userMapper.selectById(id);
        if (user != null) {
            user.setName(data.get("name"));
            user.setSignature(data.get("signature"));
            userMapper.updateById(user);

            user.setPassword(null); // 返回给前端更新本地缓存，必须脱敏
            result.put("success", true);
            result.put("data", user);
        } else {
            result.put("success", false);
        }
        return result;
    }

    /**
     * 7. 全局聚合搜索：查找用户
     */
    @GetMapping("/search")
    public Map<String, Object> searchUsers(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", keyword).or().like("id", keyword);

        List<User> list = userMapper.selectList(query);
        for(User u : list) u.setPassword(null);

        result.put("success", true);
        result.put("data", list);
        return result;
    }

    // ================== 🌟 核心更新 2：新增一键导出 Excel 接口 ==================
    /**
     * 8. 将全局用户数据导出为 Excel 花名册 (供管理员使用)
     */
    @GetMapping("/export")
    public void exportUsers(HttpServletResponse response) throws IOException {
        // 第一步：设置 HTTP 响应头，告诉浏览器我们要发送的是 Excel 文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 防止中文文件名乱码
        String fileName = URLEncoder.encode("CIMS系统全局用户花名册", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 第二步：去数据库把所有人捞出来
        List<User> users = userMapper.selectList(null);

        // 第三步：将数据库里死板的数据翻译成好看的表格数据
        List<Map<String, Object>> exportData = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("学号/工号", user.getId());
            map.put("姓名", user.getName());

            String roleName = "未知";
            if ("student".equals(user.getRole())) roleName = "学生";
            else if ("teacher".equals(user.getRole())) roleName = "教职工";
            else if ("admin".equals(user.getRole())) roleName = "管理员";
            map.put("系统角色", roleName);

            map.put("班级/部门", user.getClassName() != null ? user.getClassName() : "无");
            map.put("首次登录状态", user.getIsFirstLogin() ? "未修改密码" : "已激活");
            map.put("竞赛积分", user.getBaseScore() != null ? user.getBaseScore() : 0);

            exportData.add(map);
        }

        // 第四步：召唤阿里的 EasyExcel，把数据塞进文件流里直接打给前端！
        EasyExcel.write(response.getOutputStream())
                .head(head()) // 调用下面定义表头的方法
                .sheet("用户信息表")
                .doWrite(dataList(exportData));
    }

    // ================== 🌟 核心更新 3：EasyExcel 辅助画表工具 ==================

    // 定义 Excel 的第一行（表头字段）
    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<>();
        list.add(List.of("学号/工号"));
        list.add(List.of("姓名"));
        list.add(List.of("系统角色"));
        list.add(List.of("班级/部门"));
        list.add(List.of("首次登录状态"));
        list.add(List.of("竞赛积分"));
        return list;
    }

    // 把 Map 数据转换成 EasyExcel 要求的 List<List<Object>> 行列格式
    private List<List<Object>> dataList(List<Map<String, Object>> dataMapList) {
        List<List<Object>> list = new ArrayList<>();
        for (Map<String, Object> map : dataMapList) {
            List<Object> data = new ArrayList<>();
            data.add(map.get("学号/工号"));
            data.add(map.get("姓名"));
            data.add(map.get("系统角色"));
            data.add(map.get("班级/部门"));
            data.add(map.get("首次登录状态"));
            data.add(map.get("竞赛积分"));
            list.add(data);
        }
        return list;
    }
    // =========================================================================
}