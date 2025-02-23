package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.aq.Answer;
import com.thesis.village.model.aq.AnswerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface AnswerMapper extends BaseMapper<Answer> {


    @Select("SELECT * FROM answer WHERE qid = #{qid} AND parent_id IS NULL ORDER BY created_at DESC")
    List<AnswerVO> selectTopLevelAnswer(@Param("qid") String qid);

    @Select("SELECT * FROM answer WHERE parent_id = #{answerid} ORDER BY created_at ASC")
    List<AnswerVO> selectChildAnswer(@Param("answerid") String answerid);
}
