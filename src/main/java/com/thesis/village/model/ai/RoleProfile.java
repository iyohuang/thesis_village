package com.thesis.village.model.ai;

import com.google.common.util.concurrent.RateLimiter;
import io.github.lnyocly.ai4j.service.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yh
 */
@Data
@AllArgsConstructor
public class RoleProfile {
    private String name;
    private PlatformType platform;
    private String model;
    private Float temperature;
    private int maxTokens;
    private String systemPrompt;
    private RateLimiter rateLimiter;
}
