package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thesis.village.model.filecollection.Collections;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface CollectionMapper extends BaseMapper<Collections> {
    @Select("<script>" +
            "SELECT c.*, #{userId} AS filter_user_id " + // 添加用户ID参数传递
            "FROM collection c " +
            "<if test='userId != null'>" +
            "  INNER JOIN collection_user cu ON c.id = cu.collection_id " +
            "  AND cu.user_id = #{userId} " +
            "  AND cu.is_deleted = 0 " +
            "</if>" +
            "WHERE c.is_deleted = 0 " +
            "<if test ='currentUserId != null'>" +
            " AND c.creat_user_id = #{currentUserId} " +
            "</if>" +
            "ORDER BY deadline DESC" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "participants",
                    column = "{collectionId=id, userId=filter_user_id}", // 传递复合参数
                    many = @Many(select = "com.thesis.village.dao.CollectionUserMapper.selectParticipants"))
    })
    List<Collections> selectPageWithParticipants(@Param("userId") Long userId,@Param("currentUserId") Long currentUserId);
//    Page<Collections> selectPageWithParticipants(Page<?> page);
    
}
