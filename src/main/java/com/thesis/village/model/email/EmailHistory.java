package com.thesis.village.model.email;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yh
 */
@Data
@TableName("email_history")
@Accessors(chain = true)
public class EmailHistory {

    /**
     * 邮件ID (BIGINT UNSIGNED)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发件人邮箱
     */
    @TableField("sender_email")
    private String senderEmail;

    
    @TableField("receiver_email")
    private String receiverEmail;
    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容（HTML格式）
     */
    private String content;

    /**
     * 实际发送时间
     */
    @TableField("send_time")
    private LocalDateTime sendTime;


    @TableField("files")
    private String files;
    

    /**
     * 记录创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
