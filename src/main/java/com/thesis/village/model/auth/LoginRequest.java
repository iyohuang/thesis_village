package com.thesis.village.model.auth;

import lombok.Data;

/**
 * 登录请求对象
 * @author yh
 */
@Data
public class LoginRequest {
    private String username;
    private String password;

    // Getter 和 Setter
}
