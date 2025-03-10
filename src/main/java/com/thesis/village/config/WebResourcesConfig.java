package com.thesis.village.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yh
 */
@Configuration
public class WebResourcesConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir-avatar}")
    private String uploadDir;

    @Value("${file.upload-dir-moment}")
    private String uploadDirMoment;

    @Value("${file.upload-dir-question}")
    private String uploadDirQuestion;
    
    @Value("${file.upload-dir-collection}")
    private String uploadDirCollection;    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /uploads/avatars/** 映射到本地文件系统路径
        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations("file:" + uploadDir + "/");
        // 将 /uploads/avatars/** 映射到本地文件系统路径
        registry.addResourceHandler("/uploads/momentpics/**")
                .addResourceLocations("file:" + uploadDirMoment + "/");
        registry.addResourceHandler("/uploads/aqfiles/**")
                .addResourceLocations("file:" + uploadDirQuestion + "/");
        registry.addResourceHandler("/uploads/colfiles/**")
                .addResourceLocations("file:" + uploadDirCollection + "/");
    }
    
}
