package com.thesis.village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.thesis.village.dao.QuestionTagRelMapper;
import com.thesis.village.dao.TagMapper;
import com.thesis.village.model.aq.QuestionTagRel;
import com.thesis.village.model.aq.Tag;
import com.thesis.village.model.aq.TagDTO;
import com.thesis.village.service.TagService;
import com.thesis.village.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yh
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private QuestionTagRelMapper relMapper;
    
    public List<TagDTO> getAllTags() {
        return tagMapper.selectAllWithCount();
    }

    /**
     * 处理标签关联逻辑
     * @param qid 问题ID
     * @param tagNames 标签名称列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void processQuestionTags(Long qid, List<String> tagNames) {
        if (CollectionUtils.isEmpty(tagNames)) return;

        // 1. 标准化标签名称（去空格+转小写）
        List<String> normalizedTags = tagNames.stream()
                .map(t -> t.trim().toLowerCase())
                .distinct()
                .collect(Collectors.toList());

        // 2. 查询已存在的标签
        List<Tag> existingTags = tagMapper.selectList(
                new QueryWrapper<Tag>().in("name", normalizedTags));

        // 3. 筛选需要新增的标签
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = normalizedTags.stream()
                .filter(t -> !existingTagNames.contains(t))
                .map(t -> {
                    Tag tag = new Tag();
                    tag.setName(t.trim().toLowerCase());
                    tag.setUsageCount(0);
                    tag.setCreatedAt(LocalDateTime.now());
                    return tag;
                })
                .collect(Collectors.toList());

        // 4. 批量插入新标签
        if (!CollectionUtils.isEmpty(newTags)) {
            tagMapper.batchInsertTags(newTags);
        }

        // 5. 获取全部标签ID（包含新旧）
        List<Tag> allTags = tagMapper.selectList(
                new QueryWrapper<Tag>().in("name", normalizedTags));
        List<Long> tagIds = allTags.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        // 6. 创建关联关系
        List<QuestionTagRel> relations = tagIds.stream()
                .map(tagId -> new QuestionTagRel()
                        .setQid(qid)
                        .setTagId(tagId)
                        .setCreatedAt(LocalDateTime.now()))
                .collect(Collectors.toList());

        relMapper.batchInsertRelations(relations); // 批量插入关联
    }
}
