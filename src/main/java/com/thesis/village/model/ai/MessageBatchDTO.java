package com.thesis.village.model.ai;

import lombok.Data;

import java.util.List;

/**
 * @author yh
 */
@Data
public class MessageBatchDTO {
    private String sessionId;
    private String roleType;
    private String content;
}
