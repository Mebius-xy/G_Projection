package com.mebius.competition.controller;

import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
// @CrossOrigin 允许跨域请求。因为前端运行在 localhost:5173，后端在 localhost:8080
// 必须加这个，前端的请求才不会被浏览器拦下来
@CrossOrigin
public class TestController {

    // 把刚才做好的“铲子”拿过来用
    @Autowired
    private UserMapper userMapper;

    /**
     * 测试接口：获取所有用户列表
     * 当你在浏览器访问 http://localhost:8080/api/test/users 时，会执行这个方法
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        // userMapper.selectList(null) 相当于执行了 SQL: SELECT * FROM sys_user
        return userMapper.selectList(null);
    }
}