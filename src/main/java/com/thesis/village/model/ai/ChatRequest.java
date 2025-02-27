package com.thesis.village.model.ai;

import io.github.lnyocly.ai4j.platform.openai.chat.entity.ChatMessage;
import lombok.Data;

import java.util.List;

/**
 * @author yh
 */
@Data
public class ChatRequest {
    
    private List<ChatMessage> messages;
}
