package com.thesis.village.service.impl;

import cn.hutool.core.util.IdUtil;
import com.thesis.village.dao.AiRoleMapper;
import com.thesis.village.dao.ChatMessageMapper;
import com.thesis.village.dao.ChatSessionMapper;
import com.thesis.village.model.BusinessException;
import com.thesis.village.model.ai.*;
import com.thesis.village.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yh
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private AiRoleMapper roleMapper;
    @Autowired
    private ChatSessionMapper sessionMapper;
    @Autowired
    private ChatMessageMapper messageMapper;

    // 创建新会话
    @Transactional
    public ChatSession createSession(SessionCreateDTO dto) {
        // 校验角色是否存在
        AiRole role = roleMapper.selectRoleById(dto.getRoleId());
        if (role == null) {
            throw new BusinessException(1001,"无效的角色ID");
        }
        // 生成会话ID（使用前端传递的nanoid）
        ChatSession session = new ChatSession();
        if(dto.getId() == null){ // 如果前端没有传递id，则生成一个
            session.setId(IdUtil.nanoId());
        }else{
            session.setId(dto.getId());
        }
        session.setUserId(dto.getUserId());
        session.setRoleId(dto.getRoleId());
        session.setTitle(dto.getTitle());
        session.setDeleted(false);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        sessionMapper.insert(session);
        return session;
    }

    // 保存消息（批量）
    @Transactional
    public void saveMessages(List<MessageBatchDTO> dtos) {
        // 校验会话存在
        ChatSession session = sessionMapper.selectById(dtos.get(0).getSessionId());
//        if (session == null || session.getDeleted()) {
//            throw new BusinessException("会话不存在或已删除");
//        }
        List<ChatMessage> messages = dtos.stream()
                .map(dto -> {
                    ChatMessage msg = new ChatMessage();
                    msg.setSessionId(dto.getSessionId());
                    msg.setRoleType(dto.getRoleType());
                    msg.setContent(dto.getContent()); // 保持字段对应
                    msg.setCreatedAt(LocalDateTime.now());
                    return msg;
                })
                .collect(Collectors.toList());
        messageMapper.batchInsert(messages);
        // 更新会话时间
        session.setUpdatedAt(LocalDateTime.now());
        sessionMapper.updateById(session);
    }

    // 获取用户会话列表
    public List<ChatSession> getUserSessions(String userId) {
        return sessionMapper.selectSessionsByUser(userId);
    }

    // 获取会话消息历史
    public List<ChatMessage> getSessionMessages(String sessionId) {
        return messageMapper.selectMessagesBySession(sessionId);
    }

    @Override
    public void deleteSession(String id) {
        //逻辑删除
        sessionMapper.deleteSession(id);
    }
}
