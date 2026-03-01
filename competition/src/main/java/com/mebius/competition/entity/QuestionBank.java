package com.mebius.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 对应 MySQL 里的 sys_question_bank 表 (由 Python 爬虫导入的数据)
 */
@TableName("sys_question_bank")
public class QuestionBank {

    @TableId
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private String tag;
    private Integer views;

    // ================= 手动生成的 Getter 和 Setter =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }
}