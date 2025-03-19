package com.thesis.village.model.auth;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yh
 */

@Data
@Accessors(chain = true)
public class UserPermissionQuery {
    private Integer page = 1;
    private Integer pageSize = 10;
    //名字和role类型
    private String username;
    private String role;
}
