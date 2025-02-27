package com.thesis.village.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.thesis.village.model.ResponseResult;
import com.thesis.village.model.aq.*;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.social.Comment;
import com.thesis.village.service.AnswerService;
import com.thesis.village.service.QuestionService;
import com.thesis.village.service.TagService;
import com.thesis.village.service.UserService;
import com.thesis.village.utils.SnowflakeIdGenerator;
import com.thesis.village.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yh
 */
@RestController
@Slf4j
@RequestMapping("/aq")
public class AqController {
    @Autowired
    private TagService tagService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SnowflakeIdGenerator idGenerator;
    @Autowired
    private AnswerService answerService;
    
    @GetMapping("/tags")
    public ResponseResult<List<TagDTO>> getTags() {
        return ResponseResult.success(tagService.getAllTags());
    }

    @GetMapping("/questions")
    public ResponseResult<PageResult<QuestionDTO>> getQuestions(
            @RequestParam(required = false) String tagId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        QuestionQuery query = new QuestionQuery();
        query.setTagId(tagId);
        query.setPage(page);
        query.setPageSize(pageSize);

        return ResponseResult.success(questionService.getQuestions(query));
    }
    
    @PostMapping("/questions")
    public ResponseResult<String> askQuestion(@RequestBody Question question) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Number) map.get("id")).longValue();
        User user = userService.getUserById(id);
        question.setQid(idGenerator.generateId());
        question.setAuthorAvatar(user.getAvatar());
        question.setAuthorUid(user.getId());
        question.setAuthorName(user.getUsername());
        questionService.addQuestion(question);
        if (!CollectionUtils.isEmpty(question.getTags())) {
            tagService.processQuestionTags(question.getQid(), question.getTags());
        }
        return ResponseResult.success();
    }
    
    //获取指定id的
    @GetMapping("/questions/{qid}")
    public ResponseResult<QuestionDTO> getQuestion(@PathVariable String qid) {
        return ResponseResult.success(questionService.getQuestionById(qid));
    }
    
    @PostMapping("/answers")
    public ResponseResult<String> answerQuestion(@RequestBody Answer answer) {
        boolean success = answerService.addAnswer(answer);
        return success ? ResponseResult.success("评论成功") : ResponseResult.fail("评论失败");
    }

    @GetMapping("/questions/answers/{qid}")
    public ResponseResult<List<AnswerVO>> getComments(@PathVariable String qid) {
        List<AnswerVO> answer = answerService.getAnswersByQuestionId(qid);
        return ResponseResult.success(answer);
    }

    @PatchMapping("/answers/{aid}/toggle-accept")
    public ResponseResult<AnswerVO> toggleAcceptAnswer(@PathVariable Long aid) {
        AnswerVO answer = answerService.toggleAcceptStatus(aid);
        return ResponseResult.success(answer);
    }
}
