package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.ai.ChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {
    @Select("SELECT * FROM chat_sessions WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY updated_at DESC")
    List<ChatSession> selectSessionsByUser(String userId);
    
    //逻辑删除
    @Select("UPDATE chat_sessions SET is_deleted = 1 WHERE id = #{id}")
    void deleteSession(String id);
}
