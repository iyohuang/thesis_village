package com.thesis.village.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author yh
 */
public interface FileStorageService {
    String storeAvatarFile(@RequestParam("file") MultipartFile file) throws IOException;

    List<String> storeMomentPic(@RequestParam("images") List<MultipartFile> images) throws IOException;

    List<String> storeQuestionSrc(@RequestParam("file") List<MultipartFile> file) throws IOException;
    
    List<String> storeColSrc(List<MultipartFile> files, Long taskId, Long userId) throws IOException;

    List<String> storeEmailFile(List<MultipartFile> files) throws IOException;
}
