package com.thesis.village.model.filecollection;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yh
 */
@Data
public class CollectionCreateDTO {
    
    @NotBlank(message = "任务名称不能为空")
    @Size(max = 100, message = "任务名称最多100个字符")
    private String name;

    @NotNull(message = "截止时间不能为空")
    @Future(message = "截止时间必须是将来时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @NotEmpty(message = "至少选择一个用户")
    private List<Long> userIds;
    
    //creat_user_id
    private Long createUserId;
}
