package com.thesis.village.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author yh
 */
@Mapper
public interface UserPermissionMapper {
    
    // 根据userid和permissioncode看数量是否大于一
    @Select("select count(1) from user_permission where user_id = #{userId} and permission_code = #{permissionCode}")
    int selectCountByUserIdAndPermissionCode(@Param("userId") Long userId, @Param("permissionCode") String permissionCode);
    
}
