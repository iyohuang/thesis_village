package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thesis.village.model.aq.Question;
import com.thesis.village.model.aq.QuestionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("<script>" +
            "SELECT q.* FROM question q " +
            "<if test='tagId != null'>" +
            "INNER JOIN question_tag_rel r ON q.qid = r.qid AND r.tag_id = #{tagId} " +
            "</if>" +
            "ORDER BY q.created_at DESC " +
            "</script>")
    List<QuestionDTO> selectByCondition(@Param("tagId") String tagId);
    
    @Select("SELECT * FROM question WHERE qid = #{qid}")
    QuestionDTO selectOneByQid(@Param("qid") String qid);
    
    
}
