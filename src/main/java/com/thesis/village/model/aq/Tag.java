package com.thesis.village.model.aq;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yh
 */
@Data
@TableName("tag")
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @TableId(type = IdType.AUTO)
    private Long id;  // 业务ID如"tag_123"
    private String name;
    private String category;
    private Integer usageCount;
    private LocalDateTime createdAt;
}
