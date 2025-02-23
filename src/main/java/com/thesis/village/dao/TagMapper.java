package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.aq.Tag;
import com.thesis.village.model.aq.TagDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    @Select("SELECT t.*, COUNT(r.qid) as question_count " +
            "FROM tag t " +
            "LEFT JOIN question_tag_rel r ON t.id = r.tag_id " +
            "GROUP BY t.id " +
            "ORDER BY t.usage_count DESC")
    List<TagDTO> selectAllWithCount();

    int batchInsertTags(@Param("list") List<Tag> tags);
}
