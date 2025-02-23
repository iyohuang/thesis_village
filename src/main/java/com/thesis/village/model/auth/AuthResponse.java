package com.thesis.village.model.auth;

import lombok.Data;

/**
 * 登录响应对象
 * @author yh
 */
@Data
public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    public AuthResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter 和 Setter
}
