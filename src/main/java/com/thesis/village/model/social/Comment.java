package com.thesis.village.model.social;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 评论
 * @author yh
 */
@Data
public class Comment {
    private Long id;
    private Long momentId;
    private Long userId;
    private String userAvatar;
    private String userName;
    private String content;
    private Long parentCommentId;  // 顶级评论为 null
    private String parentUserName; // 父评论用户名
    private Date createdAt;
    private Date updatedAt;

    // 以下字段用于前端展示和点赞统计，不需要持久化到数据库
    private transient int likesCount;  // 点赞数
    private transient boolean liked;   // 当前用户是否点赞

    // 嵌套显示子评论
    private List<Comment> children;
}
