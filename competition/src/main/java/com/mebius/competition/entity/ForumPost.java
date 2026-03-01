package com.mebius.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 对应 MySQL 里的 sys_forum_post 表
 */
@TableName("sys_forum_post")
public class ForumPost {

    @TableId
    private Long id;
    private String authorId; // 发帖人的账号
    private String title;
    private String content;
    private String tag;
    private LocalDateTime createTime;

    // ================= 手动生成的 Getter 和 Setter =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}