package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.Notification;
import com.mebius.competition.entity.TeamMember;
import com.mebius.competition.mapper.NotificationMapper;
import com.mebius.competition.mapper.TeamMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private TeamMemberMapper teamMemberMapper;

    @GetMapping("/list")
    public List<Map<String, Object>> getNotifications(@RequestParam String userId) {
        List<Notification> list = notificationMapper.selectList(new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .orderByDesc("create_time"));

        // 🌟 核心修复：不再直接把原始表丢给前端，而是手动拆包，把所有长数字ID转成字符串
        List<Map<String, Object>> res = new ArrayList<>();
        for(Notification n : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(n.getId()));
            map.put("userId", n.getUserId());
            map.put("title", n.getTitle());
            map.put("content", n.getContent());
            map.put("type", n.getType());
            map.put("relatedId", String.valueOf(n.getRelatedId())); // 重点：保护队伍ID精度
            map.put("isRead", n.getIsRead());
            map.put("createTime", n.getCreateTime());
            res.add(map);
        }
        return res;
    }

    /**
     * 🌟 修复：同样改为 Map<String, Object>，防止稍后点同意时再次报错 500
     */
    @PostMapping("/handleInvite")
    public Map<String, Object> handleInvite(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();

        // 🌟 修复：安全提取
        Long notifId = Long.parseLong(data.get("notifId").toString());
        Long teamId = Long.parseLong(data.get("teamId").toString());
        String userId = data.get("userId").toString();
        String action = data.get("action").toString();

        // 1. 标记为已读
        Notification notif = notificationMapper.selectById(notifId);
        if (notif != null) {
            notif.setIsRead(1);
            notificationMapper.updateById(notif);
        }

        // 2. 更新乘客名单状态
        QueryWrapper<TeamMember> mw = new QueryWrapper<>();
        mw.eq("team_id", teamId).eq("user_id", userId);
        TeamMember member = teamMemberMapper.selectOne(mw);

        if (member != null) {
            if ("accept".equals(action)) {
                member.setStatus("joined");
                teamMemberMapper.updateById(member);
                result.put("message", "已成功加入队伍！");
            } else {
                teamMemberMapper.deleteById(member.getId());
                result.put("message", "已拒绝邀请。");
            }
        }

        result.put("success", true);
        return result;
    }
}