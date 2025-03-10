package com.thesis.village.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.thesis.village.model.filecollection.CollectionCreateDTO;
import com.thesis.village.model.filecollection.CollectionDTO;
import com.thesis.village.model.filecollection.CollectionQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author yh
 */
public interface CollectionService {
    PageInfo<CollectionDTO> getCollections(CollectionQuery query);
    boolean createCollection(CollectionCreateDTO dto);
    
    boolean updateCollection(Long id,CollectionCreateDTO dto);

    String uploadFile(MultipartFile file);
    
    boolean dosubmit(Long taskId,Long userId,List<String> colUrls) throws JsonProcessingException;

    void deleteCollectionUser(Long id, Long userId);

    void deleteCollection(Long id);
}
