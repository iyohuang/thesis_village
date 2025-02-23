package com.thesis.village.model.aq;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author yh
 */

@Data
@TableName("question_tag_rel")
@Accessors(chain = true)
public class QuestionTagRel {
    private Long id;
    private Long qid;
    private Long tagId;
    private LocalDateTime createdAt;
}
