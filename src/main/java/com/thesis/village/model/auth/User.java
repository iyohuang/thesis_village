package com.thesis.village.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author yh
 */
@Data
public class User {
    private Long id;  // 用户ID
    private String username;  // 用户名
    @JsonIgnore
    private String password;  // 加密后的密码
    private String email;  // 邮箱
    private String phoneNumber;  // 手机号
    private String avatar;  // 头像URL
    private Date createdAt;  // 创建时间
    private Date updatedAt;  // 更新时间

    // Getter 和 Setter 方法省略，使用 Lombok 可以自动生成
}

