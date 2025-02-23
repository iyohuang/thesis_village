package com.thesis.village.model.aq;

import lombok.Data;

/**
 * @author yh
 */
@Data
public class QuestionQuery {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String tagId;
}
