package com.thesis.village.config;

import com.google.common.util.concurrent.RateLimiter;
import com.thesis.village.model.ai.RoleProfile;
import io.github.lnyocly.ai4j.service.PlatformType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yh
 */
@Configuration
public class RoleConfig {
    @Bean
    public Map<String, RoleProfile> roleProfiles() {
        Map<String, RoleProfile> roles = new LinkedHashMap<>();

        // 默认角色（通用助手）
        roles.put("default", new RoleProfile(
                "通用助手",
                PlatformType.MOONSHOT,  // AI4J平台类型
                "moonshot-v1-8k",
                0.5F,
                2048,
                "你是一个通用AI助手",
                RateLimiter.create(3.0/60) // 匹配Kimi免费额度
        ));

        // 农业专家
        roles.put("agriculture", new RoleProfile(
                "农业专家",
                PlatformType.ZHIPU,
                "glm-4",
                0.2F,
                1024,
                "你是一名农业专家，回答需包含：1.问题诊断 2.解决方案 3.预防措施",
                RateLimiter.create(10.0/60)
        ));

        return Collections.unmodifiableMap(roles);
    }
}
