package com.thesis.village.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.thesis.village.aop.RequiresPermission;
import com.thesis.village.model.ResponseResult;
import com.thesis.village.model.filecollection.CollectionCreateDTO;
import com.thesis.village.model.filecollection.CollectionDTO;
import com.thesis.village.model.filecollection.CollectionQuery;
import com.thesis.village.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yh
 */
@RestController
@RequestMapping("/collections")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @GetMapping("/list")
    public ResponseResult<PageInfo<CollectionDTO>> getCollections(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long createUserId) {
        CollectionQuery query = new CollectionQuery();
        query.setPage(page);
        query.setPageSize(size);
        query.setUserId(userId);
        query.setCreateUserId(createUserId);
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return ResponseResult.success(collectionService.getCollections(query));
    }
    
    
    @RequiresPermission(value = "collection:create")
    @PostMapping
    public ResponseResult<Boolean> createCollection(
            @RequestBody @Valid CollectionCreateDTO dto) {
        return ResponseResult.success(collectionService.createCollection(dto));
    }

    @PutMapping("/{id}")
    public ResponseResult<Boolean> updateCollection(
            @PathVariable Long id,
            @RequestBody @Valid CollectionCreateDTO dto) {
        return ResponseResult.success(collectionService.updateCollection(id,dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        return ResponseResult.success("删除成功");
    }
    
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseResult<Void> deleteCollectionUser(@PathVariable Long id, @PathVariable Long userId) {
        collectionService.deleteCollectionUser(id, userId);
        return ResponseResult.success("删除成功");
    }
    
    

//    @DeleteMapping("/{id}")
//    public ResponseResult<Void> deleteCollection(@PathVariable Integer id) {
//        collectionService.deleteCollection(id);
//        return ResponseResult.success();
//    }
}
