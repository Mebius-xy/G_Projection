package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.ForumPost;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.ForumPostMapper;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forum")
@CrossOrigin
public class ForumController {

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 1. 获取所有帖子列表 (按时间倒序)
     */
    @GetMapping("/list")
    public List<Map<String, Object>> getPostList() {
        // 查出所有帖子
        List<ForumPost> posts = forumPostMapper.selectList(new QueryWrapper<ForumPost>().orderByDesc("create_time"));

        // 打包装箱，把发帖人的真实姓名和角色一起带上
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (ForumPost post : posts) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(post.getId()));
            map.put("title", post.getTitle());
            map.put("content", post.getContent());
            map.put("tag", post.getTag());
            map.put("createTime", post.getCreateTime());

            // 去找发帖人
            User author = userMapper.selectById(post.getAuthorId());
            if (author != null) {
                map.put("authorName", author.getName());
                map.put("authorRole", author.getRole());
            } else {
                map.put("authorName", "匿名用户");
                map.put("authorRole", "unknown");
            }

            resultList.add(map);
        }
        return resultList;
    }

    /**
     * 2. 发布新帖子
     */
    @PostMapping("/add")
    public Map<String, Object> addPost(@RequestBody ForumPost post) {
        Map<String, Object> result = new HashMap<>();
        post.setCreateTime(LocalDateTime.now());

        forumPostMapper.insert(post);
        result.put("success", true);
        result.put("message", "发帖成功！");
        return result;
    }
}