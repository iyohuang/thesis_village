package com.thesis.village.model.social;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Date;

/**
 * 动态
 * @author yh
 */
@Data
public class Moment {
    private Long id;
    private Long userId;
    private String userAvatar;
    private String userName;
    private String content;
    @JsonDeserialize(using = com.thesis.village.config.JsonArrayToStringDeserializer.class)
    private String images;  // JSON 字符串格式，如 '["url1","url2"]'
    private Date createdAt;
    private Date updatedAt;
    // 点赞相关
    private int likesCount = 0;  // 点赞数量，默认值为0
    private boolean liked = false;  // 当前用户是否点赞，默认值为false
}