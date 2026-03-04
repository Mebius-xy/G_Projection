package com.mebius.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
@TableName("sys_team_member")
public class TeamMember {
    @TableId
    private Long id;
    private Long teamId;
    private String userId;
    private String status;
    private LocalDateTime joinTime;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTeamId() {
        return teamId;
    }
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getJoinTime() {
        return joinTime;
    }
    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }
}
