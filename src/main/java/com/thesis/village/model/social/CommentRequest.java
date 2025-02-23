package com.thesis.village.model.social;

import lombok.Data;

/**
 * @author yh
 */
@Data
public class CommentRequest {
    private Long momentId;
    private Long userId;
    private String content;
    private Long parentCommentId; // 若为回复则有值，否则为 null
}
