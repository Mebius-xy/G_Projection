package com.mebius.competition.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mebius.competition.entity.Notification;
import com.mebius.competition.entity.Team;
import com.mebius.competition.entity.TeamMember;
import com.mebius.competition.entity.User;
import com.mebius.competition.mapper.NotificationMapper;
import com.mebius.competition.mapper.TeamMapper;
import com.mebius.competition.mapper.TeamMemberMapper;
import com.mebius.competition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/team")
@CrossOrigin
public class TeamController {

    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private TeamMemberMapper teamMemberMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 1. 创建队伍
     */
    @PostMapping("/create")
    public Map<String, Object> createTeam(@RequestBody Team team) {
        Map<String, Object> result = new HashMap<>();
        team.setCreateTime(LocalDateTime.now());
        teamMapper.insert(team); // 插入大巴车

        // 队长自己自动成为第一个乘客(已确认状态)
        TeamMember member = new TeamMember();
        member.setTeamId(team.getId());
        member.setUserId(team.getCaptainId());
        member.setStatus("joined");
        member.setJoinTime(LocalDateTime.now());
        teamMemberMapper.insert(member);

        result.put("success", true);
        result.put("message", "队伍创建成功！");
        return result;
    }

    /**
     * 2. 邀请好友上车 (发送小铃铛)
     */
    @PostMapping("/invite")
    public Map<String, Object> inviteFriend(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();

        // 安全提取数字和字符串，防止类型转换异常
        Long teamId = Long.parseLong(data.get("teamId").toString());
        String friendId = data.get("friendId").toString();
        String captainId = data.get("captainId").toString();

        Team team = teamMapper.selectById(teamId);
        User captain = userMapper.selectById(captainId);

        // 核心修复：增加空指针判断，防止找不到大巴车导致后端崩溃 (500错误)
        if (team == null || captain == null) {
            result.put("success", false);
            result.put("message", "发送失败：找不到队伍或队长！(可能是旧数据的ID精度已丢失)");
            return result;
        }

        // 新增体验优化：防止手抖重复邀请
        Long count = teamMemberMapper.selectCount(new QueryWrapper<TeamMember>()
                .eq("team_id", teamId)
                .eq("user_id", friendId));
        if (count > 0) {
            result.put("success", false);
            result.put("message", "该好友已经在队伍中或已发送过邀请！");
            return result;
        }

        // 在乘客名单里加一条 pending 的记录
        TeamMember member = new TeamMember();
        member.setTeamId(teamId);
        member.setUserId(friendId);
        member.setStatus("pending");
        teamMemberMapper.insert(member);

        // 给好友发一个小铃铛通知
        Notification notif = new Notification();
        notif.setUserId(friendId);
        notif.setTitle("队伍邀请");
        notif.setContent("你的好友 " + captain.getName() + " 邀请你加入队伍【" + team.getName() + "】！");
        notif.setType("team_invite");
        notif.setRelatedId(teamId);
        notif.setIsRead(0); // 0代表未读
        notif.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(notif);

        result.put("success", true);
        result.put("message", "邀请已发送！");
        return result;
    }

    /**
     * 3. 获取我所在的队伍列表
     */
    @GetMapping("/my")
    public List<Map<String, Object>> getMyTeams(@RequestParam String userId) {
        // 先查出我坐在哪些车上
        QueryWrapper<TeamMember> mw = new QueryWrapper<>();
        mw.eq("user_id", userId).eq("status", "joined");
        List<TeamMember> myMemberships = teamMemberMapper.selectList(mw);

        List<Map<String, Object>> teamList = new ArrayList<>();
        for (TeamMember tm : myMemberships) {
            Team team = teamMapper.selectById(tm.getTeamId());
            if(team != null){
                Map<String, Object> map = new HashMap<>();

                // 核心修复：强制将大巴车 ID 转为字符串，防止传到前端时被 JS 四舍五入丢失精度
                map.put("id", String.valueOf(team.getId()));
                map.put("name", team.getName());
                map.put("slogan", team.getSlogan());
                map.put("isCaptain", team.getCaptainId().equals(userId));

                // 查这辆车上有多少人
                long count = teamMemberMapper.selectCount(new QueryWrapper<TeamMember>().eq("team_id", team.getId()).eq("status", "joined"));
                map.put("memberCount", count);
                teamList.add(map);
            }
        }
        return teamList;
    }

    /**
     * 4. 队长解散队伍 (报废大巴车)
     */
    @PostMapping("/dismiss/{teamId}")
    public Map<String, Object> dismissTeam(@PathVariable Long teamId, @RequestParam String userId) {
        Map<String, Object> result = new HashMap<>();
        Team team = teamMapper.selectById(teamId);

        if (team != null && team.getCaptainId().equals(userId)) {
            // 1. 删除大巴车
            teamMapper.deleteById(teamId);
            // 2. 把所有乘客赶下车 (删除所有关联的组队记录)
            teamMemberMapper.delete(new QueryWrapper<TeamMember>().eq("team_id", teamId));
            result.put("success", true);
            result.put("message", "队伍已成功解散！");
        } else {
            result.put("success", false);
            result.put("message", "越权操作：只有队长可以解散队伍！");
        }
        return result;
    }

    /**
     * 5. 队员退出队伍 (跳车)
     */
    @PostMapping("/leave")
    public Map<String, Object> leaveTeam(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        Long teamId = Long.parseLong(data.get("teamId").toString());
        String userId = data.get("userId").toString();

        Team team = teamMapper.selectById(teamId);
        if (team != null && team.getCaptainId().equals(userId)) {
            result.put("success", false);
            result.put("message", "队长不能直接退出队伍，请使用【解散队伍】功能！");
            return result;
        }

        // 删除当前用户在该队伍中的记录
        int rows = teamMemberMapper.delete(new QueryWrapper<TeamMember>().eq("team_id", teamId).eq("user_id", userId));
        if (rows > 0) {
            result.put("success", true);
            result.put("message", "已成功退出队伍！");
        } else {
            result.put("success", false);
            result.put("message", "退出失败，您可能不在该队伍中。");
        }
        return result;
    }
}