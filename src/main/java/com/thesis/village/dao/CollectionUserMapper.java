package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.filecollection.CollectionUser;
import com.thesis.village.model.filecollection.ParticipantDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface CollectionUserMapper extends BaseMapper<CollectionUser> {
//    @Select("SELECT " +
//            "u.id AS userId, " + 
//            "u.username AS username, " +
//            "cu.submitted AS submitted, " +
//            "cu.submit_time AS submitTime " + 
//            "FROM collection_user cu " +
//            "JOIN user u ON cu.user_id = u.id " +
//            "WHERE cu.collection_id = #{collectionId} AND cu.is_deleted = 0 ")
    List<ParticipantDTO> selectParticipants(@Param("collectionId") Long collectionId,@Param("userId") Long userId);

    @Update("UPDATE collection_user SET is_deleted = 1 " +
            "WHERE collection_id = #{collectionId} AND user_id IN (${userIds})")
    int softDeleteRelations(@Param("collectionId") Integer collectionId,
                            @Param("userIds") String userIds);
    
    int insertBatch(List<CollectionUser> collectionUsers);
    
    @Select("SELECT user_id FROM collection_user WHERE collection_id = #{collectionId} AND is_deleted = 0")
    List<Long> selectAllUserById(@Param("collectionId") Long collectionId);
    
    //逻辑删除
    int logicDeleteUsers(@Param("collectionId") Long id,@Param("userIds") List<Long> userIds);

    int batchUpsertUsers(@Param("collectionId") Long id,@Param("userIds") List<Long> userIds);
}
