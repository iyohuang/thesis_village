package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.auth.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    // 插入新用户
    @Insert("INSERT INTO user (username, password, email, avatar, phone_number, created_at, updated_at) " +
            "VALUES (#{username}, #{password}, #{email}, #{avatar}, #{phoneNumber}, NOW(), NOW())")
    void insertUser(User user);

    // 判断用户名是否存在
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int countByUsername(@Param("username") String username);
    
    //判断邮箱是否存在
    @Select("SELECT COUNT(*) FROM user WHERE email = #{email}")
    int countByEmail(@Param("email") String email);
    
    //判断手机号是否存在
    @Select("SELECT COUNT(*) FROM user WHERE phone_number = #{phoneNumber}")
    int countByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Update("UPDATE user SET username = #{username}, phone_number = #{phoneNumber}, email = #{email}, avatar = #{avatar}, updated_at = NOW() WHERE id = #{id}")
    int updateUser(User user);

    @Update("UPDATE user SET password = #{password}, updated_at = NOW() WHERE id = #{id}")
    int updateUserPassword(User user);
    
    @Select("SELECT id,username FROM user")
    List<User> findAll();
    
    //获取所有有邮箱的用户
    @Select("SELECT id,username,email FROM user WHERE email IS NOT NULL AND is_deleted = 0")
    List<User> findAllEmail();    
}

