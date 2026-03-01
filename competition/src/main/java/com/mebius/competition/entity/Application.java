package com.mebius.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 对应 MySQL 里的 sys_application 表 (关联学生和竞赛的中间表)
 */
@TableName("sys_application")
public class Application {

    @TableId
    private Long id;
    private String studentId;
    private Long compId;
    private String statement;
    private String fileUrl;
    private String status;
    private Integer score;
    private String feedback;
    private LocalDateTime submitTime;

    // ================= 手动生成的 Getter 和 Setter =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Long getCompId() { return compId; }
    public void setCompId(Long compId) { this.compId = compId; }

    public String getStatement() { return statement; }
    public void setStatement(String statement) { this.statement = statement; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getSubmitTime() { return submitTime; }
    public void setSubmitTime(LocalDateTime submitTime) { this.submitTime = submitTime; }
}