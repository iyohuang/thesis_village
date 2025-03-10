package com.thesis.village.service.impl;

import com.thesis.village.dao.UserEmailConfigMapper;
import com.thesis.village.dao.UserMapper;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.email.UserEmailConfig;
import com.thesis.village.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * @author yh
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${file.upload-dir-email}")
    private String UPLOAD_DIR;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserEmailConfigMapper userEmailConfigMapper;
    
    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/emails/" + filename; // 返回存储的相对路径
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }
    
    //获取所有人的邮箱
    @Override
    public List<User> getAllEmail() {
        return userMapper.findAllEmail();
    }

    /**
     * 根据当前邮箱和用户id获取授权码
     */
    @Override
    public UserEmailConfig getAuthCode(String email, Long userId) {
        return userEmailConfigMapper.selectByUserIdAndEmail(userId,email);
    }    

}
