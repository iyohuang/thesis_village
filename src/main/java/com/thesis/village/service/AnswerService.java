package com.thesis.village.service;

import com.thesis.village.model.aq.Answer;
import com.thesis.village.model.aq.AnswerVO;

import java.util.List;

/**
 * @author yh
 */
public interface AnswerService {
    boolean addAnswer(Answer answer);
    
    List<AnswerVO> getAnswersByQuestionId(String qid);

    AnswerVO toggleAcceptStatus(Long aid);
}
