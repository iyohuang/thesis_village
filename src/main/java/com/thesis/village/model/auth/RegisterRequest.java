package com.thesis.village.model.auth;

import lombok.Data;

/**
 * 注册请求对象
 * @author yh
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;

    // Getter 和 Setter
}
