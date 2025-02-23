package com.thesis.village.model.aq;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yh
 */
@Data
@TableName("question")
public class Question {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long qid;      // 业务ID如"q_123"
    private String title;
    private String content;
    private Long authorUid; // 关联用户业务ID
    private Integer viewCount;
    private LocalDateTime createdAt;
    @JsonDeserialize(using = com.thesis.village.config.JsonArrayToStringDeserializer.class)
    private String files;
    private String filetype;
    private Long answerCount;
    @TableField(exist = false)
    private List<String> tags;

    // 直接平铺用户信息字段（非数据库字段）
    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String authorAvatar;
}