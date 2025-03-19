package com.thesis.village.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yh
 */
@Data
@Accessors(chain = true)
public class UserPermissionVO {
    private Long id;
    private String username;
    private String role;
    private Permissions permissions;

    // 嵌套权限类（参考前端数据结构）
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Permissions {
        private List<String> manageOthers;
        private List<String> manageSelf;
    }
}
