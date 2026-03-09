package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.Notice;
import com.mebius.competition.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 1. 获取全校公告列表 (按时间倒序，最新的在最上面)
     */
    @GetMapping("/list")
    public List<Map<String, Object>> getList() {
        List<Notice> list = noticeMapper.selectList(new QueryWrapper<Notice>().orderByDesc("publish_time"));
        List<Map<String, Object>> result = new ArrayList<>();

        for (Notice n : list) {
            Map<String, Object> map = new HashMap<>();
            // 防精度丢失
            map.put("id", String.valueOf(n.getId()));
            map.put("title", n.getTitle());
            map.put("content", n.getContent());
            map.put("type", n.getType());
            map.put("publishTime", n.getPublishTime());
            result.add(map);
        }
        return result;
    }

    /**
     * 2. 管理员发布新公告
     */
    @PostMapping("/add")
    public Map<String, Object> addNotice(@RequestBody Notice notice) {
        Map<String, Object> result = new HashMap<>();
        notice.setPublishTime(LocalDateTime.now());
        noticeMapper.insert(notice);

        result.put("success", true);
        result.put("message", "公告发布成功！");
        return result;
    }

    /**
     * 3. 管理员删除公告
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> deleteNotice(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        noticeMapper.deleteById(id);
        result.put("success", true);
        result.put("message", "公告已撤回！");
        return result;
    }
}