package com.thesis.village.service;

import com.thesis.village.model.aq.TagDTO;

import java.util.List;

/**
 * @author yh
 */
public interface TagService {

    List<TagDTO> getAllTags();

    void processQuestionTags(Long qid, List<String> tagNames);
}
