package com.thesis.village.service;

import com.thesis.village.dao.UserMapper;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.user.PasswordUpdateRequest;
import com.thesis.village.model.user.UserUpdateRequest;
import com.thesis.village.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yh
 */
public interface UserService{
    // 登录认证
    User authenticate(String username, String password);

    // 注册新用户
    void registerUser(User user);

    // 检查用户名是否已存在
    boolean isUsernameExist(String username);
    
    //检查邮箱是否存在
    boolean isEmailExist(String email);
    
    //检查电话号码是否存在
    boolean isPhoneExist(String phone);

    // 生成 JWT Token
    String generateToken(User user);

    User findByUserName(String username);

    User getUserById(Long id);
    
    boolean updateUserProfile(Long id, UserUpdateRequest updateRequest);

    boolean updatePassword(PasswordUpdateRequest request);
//    PageInfo<User> getUsers(int page, int size);
    
    List<User> getUserList();
    
}

