package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.Friend;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.FriendMapper;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friend")
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 1. 查询两人关系状态 (用于他人主页显示：加好友/已发送/已是好友)
     */
    @GetMapping("/check")
    public Map<String, Object> checkStatus(@RequestParam String userId, @RequestParam String targetId) {
        Map<String, Object> result = new HashMap<>();

        // 找找有没有 A->B 或者 B->A 的记录
        QueryWrapper<Friend> wrapper = new QueryWrapper<>();
        wrapper.and(w -> w.eq("user_id", userId).eq("friend_id", targetId))
                .or(w -> w.eq("user_id", targetId).eq("friend_id", userId));

        Friend friend = friendMapper.selectOne(wrapper);

        if (friend == null) {
            result.put("status", "none"); // 陌生人
        } else if ("accepted".equals(friend.getStatus())) {
            result.put("status", "friends"); // 已经是双向通车的好友了
        } else {
            // 如果是 pending，还得看是谁发给谁的
            if (friend.getUserId().equals(userId)) {
                result.put("status", "sent"); // 我发出去的，等对方同意
            } else {
                result.put("status", "received"); // 对方发给我的，等我同意
            }
        }
        return result;
    }

    /**
     * 2. 发起好友申请
     */
    @PostMapping("/add")
    public Map<String, Object> addFriend(@RequestBody Map<String, String> data) {
        Map<String, Object> result = new HashMap<>();
        String userId = data.get("userId");
        String targetId = data.get("targetId");

        Friend f = new Friend();
        f.setUserId(userId);
        f.setFriendId(targetId);
        f.setStatus("pending");
        f.setCreateTime(LocalDateTime.now());
        friendMapper.insert(f);

        result.put("success", true);
        return result;
    }

    /**
     * 3. 接受好友申请
     */
    @PostMapping("/accept")
    public Map<String, Object> acceptFriend(@RequestBody Map<String, Long> data) {
        Map<String, Object> result = new HashMap<>();
        Long requestId = data.get("requestId");

        Friend f = friendMapper.selectById(requestId);
        if (f != null) {
            f.setStatus("accepted");
            friendMapper.updateById(f);
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }

    /**
     * 4. 获取“我的好友”列表 (只要记录里有我，且状态是 accepted)
     */
    @GetMapping("/list")
    public List<Map<String, Object>> getFriendsList(@RequestParam String userId) {
        QueryWrapper<Friend> wrapper = new QueryWrapper<>();
        wrapper.and(w -> w.eq("user_id", userId).or().eq("friend_id", userId))
                .eq("status", "accepted");

        List<Friend> records = friendMapper.selectList(wrapper);
        List<Map<String, Object>> friends = new ArrayList<>();

        for (Friend f : records) {
            // 因为不知道我是发起方还是接收方，所以提取对方的 ID
            String otherId = f.getUserId().equals(userId) ? f.getFriendId() : f.getUserId();
            User otherUser = userMapper.selectById(otherId);

            if (otherUser != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", otherUser.getId());
                map.put("name", otherUser.getName());
                map.put("role", otherUser.getRole());
                map.put("className", otherUser.getClassName());
                map.put("signature", otherUser.getSignature());
                friends.add(map);
            }
        }
        return friends;
    }

    /**
     * 5. 获取“别人发给我的”好友申请列表
     */
    @GetMapping("/requests")
    public List<Map<String, Object>> getRequests(@RequestParam String userId) {
        QueryWrapper<Friend> wrapper = new QueryWrapper<>();
        wrapper.eq("friend_id", userId).eq("status", "pending");

        List<Friend> records = friendMapper.selectList(wrapper);
        List<Map<String, Object>> requests = new ArrayList<>();

        for (Friend f : records) {
            User requester = userMapper.selectById(f.getUserId());
            if (requester != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("requestId", String.valueOf(f.getId())); // 这个ID用来点同意的时候发给后端
                map.put("requesterId", requester.getId());
                map.put("requesterName", requester.getName());
                map.put("className", requester.getClassName());
                map.put("createTime", f.getCreateTime());
                requests.add(map);
            }
        }
        return requests;
    }
}