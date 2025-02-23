package com.thesis.village.model.user;

import lombok.Data;

/**
 * @author yh
 */
@Data
public class PasswordUpdateRequest {
    private Long id;           // 用户ID（可从Token中获取，也可以通过请求传递）
    private String oldPassword;
    private String newPassword;
}
