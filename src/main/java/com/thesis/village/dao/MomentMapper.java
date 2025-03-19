package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.social.Moment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface MomentMapper extends BaseMapper<Moment> {
    @Insert("INSERT INTO moments (user_id, user_avatar, user_name, content, images, created_at, updated_at) " +
            "VALUES (#{userId}, #{userAvatar}, #{userName}, #{content}, #{images}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertMoment(Moment moment);

    @Select("SELECT * FROM moments WHERE is_deleted = 0 ORDER BY created_at DESC")
    List<Moment> findAllMoments();
    
    //删除指定id
    @Select("DELETE FROM moments WHERE id = #{id}")
    void deleteMomentById(Long id);
}
