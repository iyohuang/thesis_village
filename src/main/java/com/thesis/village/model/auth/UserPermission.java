package com.thesis.village.model.auth;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * @author yh
 */

@Data
@Accessors(chain = true)
@TableName("user_permission")
public class UserPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("permission_code")
    private String permissionCode;
    @TableField("permission_type")
    private String permissionType;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
//    @TableLogic(value = "0", delval = "1")
    @TableField("is_deleted")
    private Integer isDeleted;
}
