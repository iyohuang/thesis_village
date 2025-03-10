package com.thesis.village.model.ai;

import lombok.Data;

/**
 * @author yh
 */
@Data
public class SessionCreateDTO {
    private String id;
    private String userId;
    private String roleId;
    private String title;
}
