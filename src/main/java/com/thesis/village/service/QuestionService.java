package com.thesis.village.service;

import com.thesis.village.model.aq.PageResult;
import com.thesis.village.model.aq.Question;
import com.thesis.village.model.aq.QuestionDTO;
import com.thesis.village.model.aq.QuestionQuery;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yh
 */
public interface QuestionService {
    PageResult<QuestionDTO> getQuestions(QuestionQuery query);

    String uploadFile(MultipartFile image);
    
    void addQuestion(Question question);

    QuestionDTO getQuestionById(String qid);
    
}
