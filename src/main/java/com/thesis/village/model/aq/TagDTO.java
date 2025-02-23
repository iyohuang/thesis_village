package com.thesis.village.model.aq;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yh
 */
@Data
@AllArgsConstructor
public class TagDTO {
    private Long id;
    private String name;
    private Long questionCount;
}
