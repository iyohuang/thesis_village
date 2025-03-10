package com.thesis.village.controller;

import cn.hutool.core.io.FileUtil;
import com.thesis.village.model.ResponseResult;
import com.thesis.village.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            List<String> fileUrl = fileStorageService.storeQuestionSrc(files);
            return ResponseResult.success("",fileUrl);
        }catch (IOException e){
            return ResponseResult.fail("文件上传失败");
        }catch (IllegalArgumentException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }
    
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        log.info("fileName: {}", fileName);
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        String filePath = "E:\\1\\thesis_test\\village_test\\uploads\\aqfiles" + File.separator + fileName;
        if (!FileUtil.exist(filePath)) {
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes); // 数组是一个字节数组，也就是文件的字节流数组
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping(value = "/upload-colfiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<List<String>> uploadCol(@RequestParam("files") List<MultipartFile> files,
                                                  @RequestParam("taskId") Long taskId,
                                                  @RequestParam("userId") Long userId) {
        try {
            List<String> fileUrl = fileStorageService.storeColSrc(files, taskId, userId);
            return ResponseResult.success("",fileUrl);
        }catch (IOException e){
            return ResponseResult.fail("文件上传失败");
        }catch (IllegalArgumentException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }
}
