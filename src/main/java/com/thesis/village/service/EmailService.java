package com.thesis.village.service;

import com.thesis.village.model.auth.User;
import com.thesis.village.model.email.EmailHistory;
import com.thesis.village.model.email.UserEmailConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author yh
 */
public interface EmailService {
    String uploadFile(MultipartFile file);

    //获取所有人的邮箱
    List<User> getAllEmail();

    UserEmailConfig getAuthCode(String email, Long userId);

    /**
     * 获得本人的历史邮件
     * @param userId
     * @param email
     * @return
     */
    List<EmailHistory> getHistoryEmail();

    boolean isExistAuthCode(Long userId, String email);

    boolean updateAuthCode(Long userId, String email, String authCode);

    List<String> getAllAvailableEmails();
}
