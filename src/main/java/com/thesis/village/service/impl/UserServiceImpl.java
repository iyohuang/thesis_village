package com.thesis.village.service.impl;

import com.thesis.village.dao.UserMapper;
import com.thesis.village.model.ResponseResult;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.user.PasswordUpdateRequest;
import com.thesis.village.model.user.UserUpdateRequest;
import com.thesis.village.service.UserService;
import com.thesis.village.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yh
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;  // MyBatis Mapper

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // 用于密码加密

    @Autowired
    private JwtUtil jwtUtil;  // JWT 工具类，用于生成 Token

    // 登录认证
    public User authenticate(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;  // 密码匹配，返回用户
        }
        return null;  // 用户名或密码错误
    }

    // 注册新用户
    public void registerUser(User user) {
        // 加密用户密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 插入数据库
        userMapper.insertUser(user);
    }

    // 检查用户名是否已存在
    public boolean isUsernameExist(String username) {
        return userMapper.countByUsername(username) > 0;
    }

    @Override
    public boolean isEmailExist(String email) {
        return userMapper.countByEmail(email) > 0;
    }

//    @Override
//    public PageInfo<User> getUsers(int page, int size) {
//        
//        PageHelper.startPage(page, size);
//        List<User> users = userMapper.findAll();
//        return new PageInfo<>(users);
//    }

    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUsername(username);
        return u;
    }

    @Override
    public boolean updateUserProfile(Long id, UserUpdateRequest updateRequest) {
        // 获取原始用户数据
        User user = userMapper.findById(id);
        if (user == null) {
            return false;
        }
        // 更新可修改字段
        user.setUsername(updateRequest.getUsername());
        user.setPhoneNumber(updateRequest.getPhoneNumber());
        user.setEmail(updateRequest.getEmail());
        user.setAvatar(updateRequest.getAvatar());
        int count = userMapper.updateUser(user);
        return count > 0;
    }

    @Override
    public boolean updatePassword(PasswordUpdateRequest request) {
        // 根据ID获取用户
        User user = userMapper.findById(request.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 校验旧密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return false;
        }
        // 编码新密码并更新
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedNewPassword);
        int count = userMapper.updateUserPassword(user);
        return count > 0;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public boolean isPhoneExist(String phone) {
        return userMapper.countByPhoneNumber(phone) > 0;
    }

    // 生成 JWT Token
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = jwtUtil.genToken(claims);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token,token,1, TimeUnit.HOURS);
        return token;
    }
}
