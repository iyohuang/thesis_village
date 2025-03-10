package com.thesis.village.model.filecollection;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("collection_user")
public class CollectionUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long collectionId;
    private Long userId;
    private Integer submitted;
    private LocalDateTime submitTime;
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;
    private List<String> files;

    public CollectionUser(Long id, Long userId) {
        this.collectionId = id;
        this.userId = userId;
        this.submitted = 0;
    }

//    public Boolean isSubmitted() {
//        return submitted == 1;
//    }
}
