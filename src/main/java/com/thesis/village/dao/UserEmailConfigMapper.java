package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.email.UserEmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface UserEmailConfigMapper extends BaseMapper<UserEmailConfig> {


    /**
     * 查询根据当前用户id和邮箱账号查询
     */
    @Select("SELECT * FROM user_email_config WHERE user_id = #{userId} AND email = #{email} AND is_deleted = 0")
    UserEmailConfig selectByUserIdAndEmail(@Param("userId") Long userId, @Param("email") String email);


    /**
     * 根据当前的邮箱和用户id绑定authcode
     */
    int updateAuthCode(@Param("userId") Long userId, @Param("email") String email, @Param("authCode") String authCode);

    /**
     * 获取有授权码且没删除的
     */
    @Select("SELECT email FROM user_email_config WHERE auth_code IS NOT NULL AND user_Id = #{userId} AND is_deleted = 0")
    List<String> selectAllAuthCode(@Param("userId") Long userId);
}
