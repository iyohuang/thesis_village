package com.thesis.village.service;

import com.thesis.village.model.ai.ChatMessage;
import com.thesis.village.model.ai.ChatSession;
import com.thesis.village.model.ai.MessageBatchDTO;
import com.thesis.village.model.ai.SessionCreateDTO;

import java.util.List;

/**
 * @author yh
 */
public interface ChatService {
    ChatSession createSession(SessionCreateDTO dto);

    void saveMessages(List<MessageBatchDTO> dtos);

    List<ChatSession> getUserSessions(String userId);

    List<ChatMessage> getSessionMessages(String sessionId);

    void deleteSession(String id);
    
    
}
