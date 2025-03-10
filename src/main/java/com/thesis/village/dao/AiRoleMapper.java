package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.ai.AiRole;
import com.thesis.village.model.ai.ChatMessage;
import com.thesis.village.model.ai.ChatSession;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface AiRoleMapper extends BaseMapper<AiRole> {
    @Select("SELECT * FROM ai_roles WHERE id = #{roleId}")
    AiRole selectRoleById(@Param("roleId") String roleId);
}



