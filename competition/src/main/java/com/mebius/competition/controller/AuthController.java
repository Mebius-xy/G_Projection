package com.mebius.competition.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Auth 是 Authentication (认证) 的缩写
 * 专门负责登录、注册、修改密码等安全相关的操作
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin // 允许跨域，给前端发通行证
public class AuthController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 处理前端发来的登录请求
     * @PostMapping 表示这个接口只接收 POST 类型的请求 (因为密码不能放在网址里明文传输)
     * @RequestBody 告诉 Spring，把前端传来的 JSON 数据自动装载到 Map 里
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        // 准备一个空盘子，用来装我们要返回给前端的消息
        Map<String, Object> result = new HashMap<>();

        // 1. 从前端发来的数据中提取账号、密码和身份
        String username = loginData.get("username");
        String password = loginData.get("password");
        String role = loginData.get("role");

        // 2. 召唤机器人 (MyBatis-Plus)，给它下达带条件的查询指令
        // QueryWrapper 就像一个条件筛选器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", username)        // 账号必须匹配
                .eq("password", password)  // 密码必须匹配
                .eq("role", role);         // 角色必须匹配

        // 让机器人去查，因为账号是主键，理论上最多只能查出一个人
        User user = userMapper.selectOne(queryWrapper);

        // 3. 判断查询结果，回复前端
        if (user != null) {
            // 查到人了！告诉前端登录成功
            result.put("success", true);
            result.put("message", "登录成功");
            // 把用户的信息（包含他是不是第一次登录、叫什么名字等）打包发给前端
            // 注意：真实开发中，绝对不能把密码也发回去！为了安全，我们要把它设为空
            user.setPassword(null);
            result.put("data", user);
        } else {
            // 没查到人，或者密码错了
            result.put("success", false);
            result.put("message", "账号、密码或身份不匹配，请重试！");
        }

        return result;
    }
}