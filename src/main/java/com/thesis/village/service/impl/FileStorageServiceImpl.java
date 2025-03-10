package com.thesis.village.service.impl;

import com.thesis.village.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yh
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${file.upload-dir-avatar}")
    private String uploadDir;

    @Value("${server.base-url}") // 新增配置
    private String baseUrl;
    
    @Autowired
    private MomentService momentService;
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private CollectionService collectionService;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * 上传文件并返回访问路径
     */
    @Override
    public String storeAvatarFile(MultipartFile file) throws IOException {
        // 确保目录存在
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名（防止文件名冲突）
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 校验文件类型（只允许图片）
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("只能上传图片文件！");
        }

        // 保存文件
        Files.copy(file.getInputStream(), filePath);

        // 返回访问路径（如 /uploads/avatars/xxx.png）
        return "/api/uploads/avatars/" + fileName;
    }

    @Override
    public List<String> storeMomentPic(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imageUrl = momentService.uploadImage(image);

                imageUrl = "/api" + imageUrl;
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }

    @Override
    public List<String> storeQuestionSrc(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String imageUrl = questionService.uploadFile(file);
                imageUrl = "/api" + imageUrl;
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }

    @Override
//    @Transactional
    public List<String> storeColSrc(List<MultipartFile> files, Long taskId, Long userId) throws IOException {
        List<String> colUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String colUrl = collectionService.uploadFile(file);
                colUrl = "/api" + colUrl;
                colUrls.add(colUrl);
            }
        }
        collectionService.dosubmit(taskId, userId, colUrls);
        return colUrls;
    }
    
    
    @Override
    public List<String> storeEmailFile(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String imageUrl = emailService.uploadFile(file);
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }
}
