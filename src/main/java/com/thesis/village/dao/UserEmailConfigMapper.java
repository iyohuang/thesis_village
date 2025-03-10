package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.email.UserEmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author yh
 */
@Mapper
public interface UserEmailConfigMapper extends BaseMapper<UserEmailConfig> {
    
    
    //查询根据当前用户id和邮箱账号查询
    @Select("SELECT * FROM user_email_config WHERE user_id = #{userId} AND email = #{email} AND is_deleted = 0")
    UserEmailConfig selectByUserIdAndEmail(Long userId, String email);
}
