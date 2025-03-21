package com.thesis.village.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author yh
 */

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter { // 继承跨域请求的类

    @Override
    public void addCorsMappings(CorsRegistry registry) { // 跨域处理的方法
        registry.addMapping("/**") // 任意访问都允许跨域
                .allowedOrigins("http://localhost:8080", "null") // 跨域来源
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE","PATCH") // 跨域请求类型
                .allowedHeaders("*")
                .maxAge(3600) // 超时时间
                .allowCredentials(true); // 允许携带信息
    }
}
