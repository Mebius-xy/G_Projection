package com.mebius.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("sys_user")
public class User {

    @TableId
    private String id;
    private String password;
    private String name;
    private String role;
    private Boolean isFirstLogin;
    private String className;
    private Integer baseScore;
    private LocalDateTime createTime;

    // 🌟 新增：个性签名
    private String signature;

    // ================= 手动生成的 Getter 和 Setter =================
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getIsFirstLogin() { return isFirstLogin; }
    public void setIsFirstLogin(Boolean isFirstLogin) { this.isFirstLogin = isFirstLogin; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Integer getBaseScore() { return baseScore; }
    public void setBaseScore(Integer baseScore) { this.baseScore = baseScore; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    // 🌟 新增的 Getter / Setter
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}