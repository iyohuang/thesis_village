package com.thesis.village.model.auth;

import lombok.Data;

/**
 * @author yh
 */
@Data
public class UserPermissionDTO {
    private Long id;
    private String username;
    private String role;
    private String manageOthers;  // 逗号分隔的权限字符串
    private String manageSelf;
}
