package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.ai.ChatMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    @Select("SELECT * FROM chat_messages WHERE session_id = #{sessionId} ORDER BY created_at ASC")
    List<ChatMessage> selectMessagesBySession(String sessionId);

    @Insert("<script>" +
            "INSERT INTO chat_messages (session_id, role_type, content) VALUES " +
            "<foreach collection='messages' item='msg' separator=','>" +
            "(#{msg.sessionId}, #{msg.roleType}, #{msg.content})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("messages") List<ChatMessage> messages);
}
