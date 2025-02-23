package com.thesis.village.dao;

import com.thesis.village.model.social.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comments WHERE moment_id = #{momentId} AND parent_comment_id IS NULL ORDER BY created_at DESC")
    List<Comment> findTopLevelComments(@Param("momentId") Long momentId);

    @Select("SELECT * FROM comments WHERE parent_comment_id = #{parentId} ORDER BY created_at ASC")
    List<Comment> findChildComments(@Param("parentId") Long parentId);

    @Insert("INSERT INTO comments (moment_id, user_id, user_avatar, user_name, content, parent_comment_id, parent_user_name, created_at, updated_at) " +
            "VALUES (#{momentId}, #{userId}, #{userAvatar}, #{userName}, #{content}, #{parentCommentId}, #{parentUserName}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertComment(Comment comment);
    
    @Select("SELECT * FROM comments WHERE id = #{parentId}")
    Comment findByParentId(@Param("parentId") Long parentId);
}
