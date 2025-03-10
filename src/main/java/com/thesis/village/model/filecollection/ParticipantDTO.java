package com.thesis.village.model.filecollection;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTO {
    Long userId;
    Boolean submitted;
    LocalDateTime submitTime;
    private List<String> files;
}
