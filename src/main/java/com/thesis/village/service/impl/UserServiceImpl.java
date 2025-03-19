package com.thesis.village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thesis.village.dao.UserMapper;
import com.thesis.village.dao.UserPermissionMapper;
import com.thesis.village.model.ai.SessionCreateDTO;
import com.thesis.village.model.auth.*;
import com.thesis.village.model.user.PasswordUpdateRequest;
import com.thesis.village.model.user.UserUpdateRequest;
import com.thesis.village.service.ChatService;
import com.thesis.village.service.UserService;
import com.thesis.village.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yh
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;  // MyBatis Mapper

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // 用于密码加密

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private JwtUtil jwtUtil;  // JWT 工具类，用于生成 Token
    
    @Autowired
    private UserPermissionMapper userPermissionMapper;

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
        // 查出当前用户的id
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User user1 = userMapper.selectOne(queryWrapper);
        // 为每个用户搞3个智能助手
        createDefaultAiRoles(user1.getId());
    }
    
    public void createDefaultAiRoles(Long userId){
        // 创建默认角色
        List<Map<String, String>> defaultRoles = new ArrayList<>();

        // 创建农业专家Map
        Map<String, String> role1 = new HashMap<>();
        role1.put("roleKey", "agriculture");
        role1.put("title", "农业专家");

        // 创建通用助手Map
        Map<String, String> role2 = new HashMap<>();
        role2.put("roleKey", "default");
        role2.put("title", "通用助手");

        // 创建税务助手Map
        Map<String, String> role3 = new HashMap<>();
        role3.put("roleKey", "tax");
        role3.put("title", "税务助手");

        defaultRoles.add(role1);
        defaultRoles.add(role2);
        defaultRoles.add(role3);

        defaultRoles.forEach(role -> {
            // 创建会话DTO
            SessionCreateDTO dto = new SessionCreateDTO();
            dto.setUserId(userId.toString());
            dto.setRoleId(role.get("roleKey"));
            dto.setTitle(role.get("title"));

            // 调用会话创建服务
            chatService.createSession(dto);
        });
        
        
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

    @Override
    public List<User> getUserList() {
        return userMapper.findAll();
    }
    
    @Override
    public PageInfo<UserPermissionVO> getUserPermissions(UserPermissionQuery query){
        PageHelper.startPage(query.getPage(), query.getPageSize());
        List<UserPermissionDTO> dtos = userPermissionMapper.selectUsersWithPermissions(query.getUsername(), query.getRole());
        PageInfo<UserPermissionDTO> pageInfo = new PageInfo<>(dtos);
        List<UserPermissionVO> collect = dtos.stream().map(dto ->
                new UserPermissionVO()
                        .setId(dto.getId())
                        .setUsername(dto.getUsername())
                        .setRole(dto.getRole())
                        .setPermissions(new UserPermissionVO.Permissions(
                                splitToList(dto.getManageOthers()),
                                splitToList(dto.getManageSelf())
                        ))
        ).collect(Collectors.toList());
        PageInfo<UserPermissionVO> dtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, dtoPageInfo);
        dtoPageInfo.setList(collect);
        return dtoPageInfo;
    }

    private List<String> splitToList(String str) {
        return Optional.ofNullable(str)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.asList(s.split(",")))
                .orElse(Collections.emptyList());
    }

    @Transactional
    @Override
    public void updatePermissions(Long userId, UserPermissionVO.Permissions permissions) {
        // 处理两种权限类型
        processPermissionType(userId, permissions.getManageOthers(), "manage_others");
        processPermissionType(userId, permissions.getManageSelf(), "manage_self");
    }

    private void processPermissionType(Long userId, List<String> newPermissions, String type) {

        QueryWrapper<UserPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserPermission::getUserId, userId).eq(UserPermission::getPermissionType, type);
        // 1. 获取现有权限（包含软删除的）
        List<UserPermission> existPermissions = userPermissionMapper.selectList(queryWrapper);
        
        // 2. 构建新旧权限映射
        Map<String, UserPermission> existMap = existPermissions.stream()
                .collect(Collectors.toMap(UserPermission::getPermissionCode, Function.identity()));
        log.info("existMap: {}", existMap);
        log.info("userid:{}", userId);
        log.info("existPermissions:{}", existPermissions);
        
        // 3. 处理需要保留的权限
        Set<String> toKeep = new HashSet<>(newPermissions);
        existMap.forEach((code, perm) -> {
            if (toKeep.contains(code)) {
                if (perm.getIsDeleted() == 1) {
                    perm.setIsDeleted(0);
                    userPermissionMapper.updateById(perm);
                }
                toKeep.remove(code);
            } else {
                if (perm.getIsDeleted() == 0) {
                    perm.setIsDeleted(1);
                    userPermissionMapper.updateById(perm);
                }
            }
        });

        // 4. 新增剩余的权限
        toKeep.forEach(code -> {
            UserPermission newPerm = new UserPermission()
                    .setUserId(userId)
                    .setPermissionCode(code)
                    .setPermissionType(type)
                    .setIsDeleted(0)
                    .setUpdateTime(LocalDateTime.now());
            userPermissionMapper.insert(newPerm);
        });
    }

    @Override
    public Boolean updateUserRole(Long userId, UserPermissionDTO dto) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(User::getId, userId).set(User::getRoleType, dto.getRole());
        int update = userMapper.update(null, updateWrapper);
        return update > 0;

    }
}
