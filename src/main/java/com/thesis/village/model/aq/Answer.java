package com.thesis.village.model.aq;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yh
 */
@Data
@Accessors(chain = true)
@TableName("answer")
public class Answer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long answerId;
    private Long qid;
    private Long authorUid;
    private String authorAvatar;
    private String authorName;
    private String content;
    private Long parentId;
    private String parentUserName;
    private Integer isAccepted;
    private Integer upvoteCount;
    private Integer status;
    private String createdAt;
    @JsonDeserialize(using = com.thesis.village.config.JsonArrayToStringDeserializer.class)
    private String files;
    private String filetype;
}
