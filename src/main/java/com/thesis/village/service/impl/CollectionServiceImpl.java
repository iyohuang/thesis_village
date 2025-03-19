package com.thesis.village.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thesis.village.dao.CollectionMapper;
import com.thesis.village.dao.CollectionUserMapper;
import com.thesis.village.model.filecollection.*;
import com.thesis.village.service.CollectionService;
import com.thesis.village.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yh
 */
@Service
@Slf4j
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;
    @Autowired
    private CollectionUserMapper collectionUserMapper;

    @Value("${file.upload-dir-collection}")
    private String UPLOAD_DIR;
    @Transactional
    public boolean createCollection(CollectionCreateDTO dto) {
        // 验证用户有效性
//        List<Long> validUserIds = userMapper.selectBatchIds(dto.getUserIds())
//                .stream()
//                .map(User::getId)
//                .collect(Collectors.toList());
//
//        if (validUserIds.size() != dto.getUserIds().size()) {
//            throw new BusinessException("存在无效用户");
//        }
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Number) map.get("id")).longValue();
        // 插入主记录
        Collections collection = new Collections();
        collection.setName(dto.getName());
        collection.setDeadline(dto.getDeadline());
        collection.setCreateTime(LocalDateTime.now());
        collection.setCreatUserId(id);
        collectionMapper.insert(collection);

        // 批量插入关联关系
        List<CollectionUser> relations = dto.getUserIds().stream()
                .distinct()
                .map(userId -> new CollectionUser(collection.getId(), userId))
                .collect(Collectors.toList());

        return collectionUserMapper.insertBatch(relations) > 0;
    }

    public PageInfo<CollectionDTO> getCollections(CollectionQuery query) {
        // 启动分页（参数顺序：pageNum, pageSize）
        PageHelper.startPage(query.getPage(), query.getPageSize());
        // 执行查询（返回 Page<Collections>）
        List<Collections> collections = collectionMapper.selectPageWithParticipants(query.getUserId(),query.getCreateUserId());
        // 转换为 PageInfo 对象（包含 total、pages 等分页信息）
        PageInfo<Collections> pageInfo = new PageInfo<>(collections);
        
        // 将 List<Collections> 转换为 List<CollectionDTO>
        List<CollectionDTO> dtoList = collections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 将分页结果复制到新的 PageInfo<CollectionDTO>
        PageInfo<CollectionDTO> dtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, dtoPageInfo);
        dtoPageInfo.setList(dtoList);

        return dtoPageInfo;
    }

    private CollectionDTO convertToDTO(Collections collection) {
        CollectionDTO dto = new CollectionDTO();
        BeanUtils.copyProperties(collection, dto);
        dto.setStatus(calculateStatus(collection.getDeadline()));
        return dto;
    }

    private String calculateStatus(LocalDateTime deadline) {
        return deadline.isAfter(LocalDateTime.now()) ? "进行中" : "已截止";
    }

    @Override
    @Transactional
    public boolean updateCollection(Long id,CollectionCreateDTO dto) {
        List<Long> existingUserIds = collectionUserMapper.selectAllUserById(id);
        List<Long> newUserIds = dto.getUserIds(); // 前端传入的当前选中用户
        
        List<Long> usersToAdd = newUserIds.stream()
                .filter(t -> !existingUserIds.contains(t))
                .collect(Collectors.toList());

        List<Long> usersToRemove = existingUserIds.stream()
                .filter(t -> !newUserIds.contains(t))
                .collect(Collectors.toList());

        if (!usersToRemove.isEmpty()) {
            collectionUserMapper.logicDeleteUsers(id,usersToRemove);
        }

        if (!usersToAdd.isEmpty()) {
            collectionUserMapper.batchUpsertUsers(id,usersToAdd);
        }
        UpdateWrapper<Collections> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.set("name",dto.getName());
        updateWrapper.set("deadline",dto.getDeadline());
        collectionMapper.update(null,updateWrapper);
        return true;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/colfiles/" + filename; // 返回存储的相对路径
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }

    @Override
    public boolean dosubmit(Long taskId, Long userId, List<String> colUrls) throws JsonProcessingException {
        UpdateWrapper<CollectionUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("collection_id",taskId);
        updateWrapper.eq("user_id",userId);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFiles = objectMapper.writeValueAsString(colUrls);
        updateWrapper.set("files", jsonFiles);
        updateWrapper.set("submitted",1);
        updateWrapper.set("submit_time", LocalDateTime.now());
        return collectionUserMapper.update(null,updateWrapper) > 0;
    }

    @Override
    public void deleteCollectionUser(Long id, Long userId) {
        // Mybatis Plus 根据 collection_id 和 user_id 删除记录
        UpdateWrapper<CollectionUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("collection_id", id);
        updateWrapper.eq("user_id", userId);
        updateWrapper.set("submitted", 0);
        updateWrapper.set("submit_time", null);
        updateWrapper.set("files", null);
        collectionUserMapper.update(null, updateWrapper);
    }

    @Override
    public void deleteCollection(Long id) {
        collectionMapper.deleteById(id);
    }
}
