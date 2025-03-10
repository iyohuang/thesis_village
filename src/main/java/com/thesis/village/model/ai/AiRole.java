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
@TableName("ai_roles")
public class AiRole {
    @TableId(type = IdType.INPUT)
    private String id;
    private String name;
    private String icon;
    private String prompt;
    @TableField("is_default")
    private Boolean isDefault;
    private LocalDateTime createdAt;
}
