package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.ForumPost;
import com.mebius.competition.entity.ForumComment;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.ForumPostMapper;
import com.mebius.competition.mapper.ForumCommentMapper;
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

    // 🌟 新增：注入评论的搬运工
    @Autowired
    private ForumCommentMapper forumCommentMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 1. 获取所有帖子列表 (加上了该帖子有多少条评论的统计)
     */
    @GetMapping("/list")
    public List<Map<String, Object>> getPostList() {
        List<ForumPost> posts = forumPostMapper.selectList(new QueryWrapper<ForumPost>().orderByDesc("create_time"));
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (ForumPost post : posts) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(post.getId()));
            map.put("title", post.getTitle());
            map.put("content", post.getContent());
            map.put("tag", post.getTag());
            map.put("createTime", post.getCreateTime());

            User author = userMapper.selectById(post.getAuthorId());
            if (author != null) {
                map.put("authorName", author.getName());
                map.put("authorRole", author.getRole());
            } else {
                map.put("authorName", "匿名用户");
                map.put("authorRole", "unknown");
            }

            // 🌟 新增：计算这个帖子下面有几条评论
            long commentCount = forumCommentMapper.selectCount(new QueryWrapper<ForumComment>().eq("post_id", post.getId()));
            map.put("commentCount", commentCount);

            resultList.add(map);
        }
        return resultList;
    }

    /**
     * 2. 发布新帖子 (保持不变)
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

    /**
     * 3. 🌟 新增：获取某个帖子的所有评论 (要附带评论人的名字)
     */
    @GetMapping("/comments/{postId}")
    public List<Map<String, Object>> getCommentsByPostId(@PathVariable Long postId) {
        // 按照时间正序排列，最先评论的在最上面（一楼、二楼...）
        QueryWrapper<ForumComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId).orderByAsc("create_time");

        List<ForumComment> comments = forumCommentMapper.selectList(queryWrapper);
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (ForumComment comment : comments) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(comment.getId()));
            map.put("content", comment.getContent());
            map.put("createTime", comment.getCreateTime());
            map.put("authorId", comment.getAuthorId()); // 把ID传回去，方便前端点击头像跳公共主页

            // 查出是谁评论的
            User author = userMapper.selectById(comment.getAuthorId());
            if (author != null) {
                map.put("authorName", author.getName());
                map.put("authorRole", author.getRole());
            } else {
                map.put("authorName", "已注销用户");
                map.put("authorRole", "unknown");
            }
            resultList.add(map);
        }
        return resultList;
    }

    /**
     * 4. 🌟 新增：发表一条新评论
     */
    @PostMapping("/addComment")
    public Map<String, Object> addComment(@RequestBody ForumComment comment) {
        Map<String, Object> result = new HashMap<>();
        comment.setCreateTime(LocalDateTime.now());

        forumCommentMapper.insert(comment);
        result.put("success", true);
        result.put("message", "评论发表成功！");
        return result;
    }

    /**
     * 🌟 新增：删除帖子及其相关评论
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> deletePost(@PathVariable Long id, @RequestParam String userId, @RequestParam String role) {
        Map<String, Object> result = new HashMap<>();
        ForumPost post = forumPostMapper.selectById(id);

        if (post != null) {
            // 只有发帖人自己，或者超级管理员才能删帖！
            if (post.getAuthorId().equals(userId) || "admin".equals(role)) {
                forumPostMapper.deleteById(id);
                // 连带把盖的楼（评论）也一起推平
                forumCommentMapper.delete(new QueryWrapper<ForumComment>().eq("post_id", id));
                result.put("success", true);
                result.put("message", "帖子已永久删除！");
            } else {
                result.put("success", false);
                result.put("message", "越权操作：您没有权限删除此贴！");
            }
        } else {
            result.put("success", false);
            result.put("message", "帖子不存在！");
        }
        return result;
    }
}