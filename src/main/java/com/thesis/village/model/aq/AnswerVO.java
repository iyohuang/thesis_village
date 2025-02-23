package com.thesis.village.model.aq;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.thesis.village.model.social.Comment;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yh
 */
@Data
@Accessors(chain = true)
public class AnswerVO {
    private String id;
    private String answerId;
    private String qid;
    private String authorUid;
    private String authorAvatar;
    private String authorName;
    private String content;
    private String parentId;
    private String parentUserName;
    private boolean isAccepted;
    private String upvoteCount;
    private String status;
    private String createdAt;
    @JsonDeserialize(using = com.thesis.village.config.JsonArrayToStringDeserializer.class)
    private String files;
    private String filetype;
    private List<AnswerVO> children;
}
