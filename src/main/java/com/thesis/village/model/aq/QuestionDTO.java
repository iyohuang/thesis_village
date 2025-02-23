package com.thesis.village.model.aq;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yh
 */
@Data
@Accessors(chain = true)
public class QuestionDTO {
    private String qid;
    private String title;
    private String content;
    private Long authorUid;
    private String authorName;
    private String authorAvatar;
    private List<Long> tags;
    private String answerCount;
    private LocalDateTime createdAt;
    private String filetype;
    @JsonDeserialize(using = com.thesis.village.config.JsonArrayToStringDeserializer.class)
    private String files;
}
