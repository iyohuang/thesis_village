package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.aq.QuestionTagRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yh
 */
@Mapper
public interface QuestionTagRelMapper extends BaseMapper<QuestionTagRel> {
    /**
     * 根据问题ID获取标签ID列表
     * @param qid 问题业务ID
     * @return 标签ID集合
     */
    List<Long> selectTagIdsByQid(@Param("qid") String qid);

    int batchInsertRelations(@Param("list") List<QuestionTagRel> relations);

    List<QuestionTagRel> batchSelectTagsByQids(@Param("qids") List<String> qids);
}
