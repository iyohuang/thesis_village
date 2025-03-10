package com.thesis.village.model.ai;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yh
 */
@Data
@TableName("chat_sessions")
public class ChatSession {
    @TableId(type = IdType.INPUT)
    private String id;
    private String userId;
    private String roleId;
    private String title;
    @TableField("is_deleted")
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}