package com.mebius.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 对应 MySQL 里的 sys_permission_approval 表 (记录教师的数据权限申请)
 */
@TableName("sys_permission_approval")
public class PermissionApproval {

    @TableId
    private Long id;
    private String teacherId;
    private String targetClass;
    private String reason;
    private String status; // pending, approved, rejected
    private LocalDateTime applyTime;

    // ================= 手动生成的 Getter 和 Setter =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getTargetClass() { return targetClass; }
    public void setTargetClass(String targetClass) { this.targetClass = targetClass; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getApplyTime() { return applyTime; }
    public void setApplyTime(LocalDateTime applyTime) { this.applyTime = applyTime; }
}