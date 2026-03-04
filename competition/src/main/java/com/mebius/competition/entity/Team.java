package com.mebius.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
@TableName("sys_team")
public class Team {
    @TableId
    private Long id;
    private String name;
    private String captainId;
    private String slogan;
    private LocalDateTime createTime;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCaptainId() {
        return captainId;
    }
    public void setCaptainId(String captainId) {
        this.captainId = captainId;
    }
    public String getSlogan() {
        return slogan;
    }
    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
