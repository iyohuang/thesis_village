package com.thesis.village.model.filecollection;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yh
 */
@Data
@TableName("collection")
public class Collections {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private LocalDateTime deadline;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;
    @TableField("creat_user_id")
    private Long creatUserId;
    @TableField(exist = false)
    private List<ParticipantDTO> participants;
}
