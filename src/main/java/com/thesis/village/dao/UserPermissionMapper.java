package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.auth.UserPermission;
import com.thesis.village.model.auth.UserPermissionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface UserPermissionMapper extends BaseMapper<UserPermission> {
    
    // 根据userid和permissioncode看数量是否大于一
    @Select("select count(1) from user_permission where user_id = #{userId} and permission_code = #{permissionCode} and is_deleted = 0")
    int selectCountByUserIdAndPermissionCode(@Param("userId") Long userId, @Param("permissionCode") String permissionCode);
    
    List<UserPermissionDTO> selectUsersWithPermissions(@Param("username") String username, @Param("role") String role);
}
