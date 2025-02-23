package com.thesis.village.model.user;

import lombok.Data;

/**
 * @author yh
 */
@Data
public class UserUpdateRequest {
    private String username;
    private String phoneNumber;
    private String email;
    private String avatar;
}
