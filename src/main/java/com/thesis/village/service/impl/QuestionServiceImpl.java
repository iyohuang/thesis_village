package com.thesis.village.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thesis.village.dao.QuestionMapper;
import com.thesis.village.dao.QuestionTagRelMapper;
import com.thesis.village.dao.UserMapper;
import com.thesis.village.model.aq.*;
import com.thesis.village.model.auth.User;
import com.thesis.village.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yh
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionTagRelMapper relMapper;
    @Autowired
    private UserMapper userMapper; // 假设有用户服务客户端
    @Value("${file.upload-dir-question}")
    private String UPLOAD_DIR;
    public PageResult<QuestionDTO> getQuestions(QuestionQuery query) {
        // 启动分页（必须紧跟在查询方法前）
        PageHelper.startPage(query.getPage(), query.getPageSize());

        // 执行查询（此时SQL会自动添加LIMIT）
        List<QuestionDTO> questions = questionMapper.selectByCondition(query.getTagId());

        // 转换为Page对象获取分页信息
        PageInfo<QuestionDTO> pageInfo = new PageInfo<>(questions);

        log.info("questions为,{}",questions);
        log.info("pageInfo为,{}",pageInfo);
//        List<Long> qids = questions.stream()
//                .map(QuestionDTO::getQid)
//                .collect(Collectors.toList());
//        //获取标签N+1优化
//        Map<Long, List<Long>> tagMap = relMapper.batchSelectTagsByQids(qids)
//                .stream()
//                .collect(Collectors.groupingBy(
//                        QuestionTagRel::getQid,
//                        Collectors.mapping(QuestionTagRel::getTagId, Collectors.toList())
//                ));
//        
//        log.info("tagMap为,{}",tagMap);
        
        List<QuestionDTO> dtos = questions.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            dto.setQid(q.getQid());
            dto.setTitle(q.getTitle());
            dto.setContent(q.getContent());
            dto.setCreatedAt(q.getCreatedAt());
            dto.setFiles(q.getFiles());
            dto.setFiletype(q.getFiletype());
            // 获取作者信息
            User u = userMapper.findById(q.getAuthorUid());
            dto.setAuthorUid(u.getId());
            dto.setAuthorAvatar(u.getAvatar());
            dto.setAuthorName(u.getUsername());
            // 获取标签
            List<Long> tagIds = relMapper.selectTagIdsByQid(q.getQid());
            dto.setTags(tagIds);

//             获取回答数（需要实现answerMapper）
//            dto.setAnswerCount(answerMapper.countByQid(q.getQid()));

            return dto;
        }).collect(Collectors.toList());
        return new PageResult<>(
                pageInfo.getTotal(),
                dtos,
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.isHasPreviousPage(),
                pageInfo.isHasNextPage(),
                pageInfo.getPages()
        );
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/aqfiles/" + filename; // 返回存储的相对路径
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }

    @Override
    public void addQuestion(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public QuestionDTO getQuestionById(String qid) {
        QuestionDTO questionDTO = questionMapper.selectOneByQid(qid);
        if (questionDTO != null) {
            User user = userMapper.findById(questionDTO.getAuthorUid());
            questionDTO.setAuthorName(user.getUsername());
            questionDTO.setAuthorAvatar(user.getAvatar());
            List<Long> tagIds = relMapper.selectTagIdsByQid(questionDTO.getQid());
            questionDTO.setTags(tagIds);
            log.info("questionDTO为,{}",questionDTO);
            return questionDTO;
        }
        return null;
    }
}
