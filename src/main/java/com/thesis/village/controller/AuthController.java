package com.thesis.village.controller;
import com.thesis.village.enums.HttpStatusEnum;
import com.thesis.village.model.ResponseResult;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.auth.AuthResponse;
import com.thesis.village.model.auth.LoginRequest;
import com.thesis.village.model.auth.RegisterRequest;
import com.thesis.village.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;  // 用户服务层

    // 登录接口
    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("Received login request: {}", loginRequest);
        // 校验用户名和密码
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return ResponseResult.fail(HttpStatusEnum.UNAUTHORIZED);
        }

        // 生成 JWT
        String token = userService.generateToken(user);
        return ResponseResult.success("",token);
        // 返回成功和 Token
    }

    // 注册接口
    @PostMapping("/register")
    public ResponseResult<?> register(@RequestBody RegisterRequest registerRequest) {
        log.info("Received register request: {}", registerRequest);
        // 检查用户名是否已存在
        if (userService.isUsernameExist(registerRequest.getUsername())) {
            return ResponseResult.fail("Username already exists");
        }
        // 检查邮箱是否已经存在
        if (userService.isEmailExist(registerRequest.getEmail())) {
            return ResponseResult.fail("Email already exists");
        }
        // 检查手机号是否已经存在
        if (userService.isPhoneExist(registerRequest.getPhoneNumber())) {
            return ResponseResult.fail("Phone number already exists");
        }
        // 创建并注册新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setAvatar("/api/uploads/avatars/default.jpg");
        userService.registerUser(user);
        return ResponseResult.success("User registered successfully");
    }
}
