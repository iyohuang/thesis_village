package com.thesis.village.controller;

import com.thesis.village.model.ResponseResult;
import com.thesis.village.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author yh
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileUploadController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileStorageService.storeAvatarFile(file);
            return ResponseResult.success("",fileUrl);
        }catch (IOException e){
            return ResponseResult.fail("文件上传失败");
        }catch (IllegalArgumentException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    @PostMapping(value = "/upload-momentpic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<List<String>> uploadMomentPic(@RequestParam("images") List<MultipartFile> images) {
        try {
            List<String> fileUrl = fileStorageService.storeMomentPic(images);
            return ResponseResult.success("",fileUrl);
        }catch (IOException e){
            return ResponseResult.fail("文件上传失败");
        }catch (IllegalArgumentException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    @PostMapping(value = "/upload-aqfiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<List<String>> uploadAqFile(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> fileUrl = fileStorageService.storeMomentPic(files);
            return ResponseResult.success("",fileUrl);
        }catch (IOException e){
            return ResponseResult.fail("文件上传失败");
        }catch (IllegalArgumentException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }
}
