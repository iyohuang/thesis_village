package com.thesis.village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.thesis.village.dao.AnswerMapper;
import com.thesis.village.dao.QuestionMapper;
import com.thesis.village.dao.UserMapper;
import com.thesis.village.model.aq.Answer;
import com.thesis.village.model.aq.AnswerVO;
import com.thesis.village.model.aq.Question;
import com.thesis.village.model.auth.User;
import com.thesis.village.service.AnswerService;
import com.thesis.village.service.QuestionService;
import com.thesis.village.utils.SnowflakeIdGenerator;
import com.thesis.village.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yh
 */
@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private SnowflakeIdGenerator idGenerator;
    @Override
    public boolean addAnswer(Answer answer) {
        answer.setAnswerId(idGenerator.generateId());
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Number) map.get("id")).longValue();
        User u = userMapper.findById(id);
        answer.setAuthorUid(id);
        answer.setAuthorAvatar(u.getAvatar());
        answer.setAuthorName(u.getUsername());
        if(answer.getParentId()!=null){
            QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Answer::getAnswerId, answer.getParentId());
            Answer parent = answerMapper.selectOne(queryWrapper);
            answer.setParentId(parent.getAnswerId());
            answer.setParentUserName(parent.getAuthorName());
            int result = answerMapper.insert(answer);
            return result > 0;
        }
        int result = answerMapper.insert(answer);
            if(result > 0){
                UpdateWrapper<Question> updateWrapper = new UpdateWrapper();
                updateWrapper.lambda().eq(Question::getQid, answer.getQid());
                updateWrapper.lambda().setIncrBy(Question::getAnswerCount, 1);
                questionMapper.update(null, updateWrapper);
            }
        return result > 0;
        
    }

    @Override
    public List<AnswerVO> getAnswersByQuestionId(String qid) {
        List<AnswerVO> topAnswer = answerMapper.selectTopLevelAnswer(qid);
        for(AnswerVO a : topAnswer){
            a.setChildren(getChildAnswer(a.getAnswerId()));
        }
        return topAnswer;
    }
    
    private List<AnswerVO> getChildAnswer(String answerid){
        List<AnswerVO> childAnswer = answerMapper.selectChildAnswer(answerid);
        for(AnswerVO child : childAnswer){
            child.setChildren(getChildAnswer(child.getAnswerId()));
        }
        return childAnswer;
    }

    @Override
    public AnswerVO toggleAcceptStatus(Long aid) {
        QueryWrapper<Answer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Answer::getAnswerId, aid);
        Answer answer = answerMapper.selectOne(queryWrapper);
        if(answer == null){
            return null;
        }
        answer.setIsAccepted(answer.getIsAccepted()== 0?1:0);
        answerMapper.updateById(answer);
        return new AnswerVO().setAccepted(answer.getIsAccepted() == 1);
    }
}
