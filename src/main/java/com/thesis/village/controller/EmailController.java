package com.thesis.village.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.util.MailSSLSocketFactory;
import com.thesis.village.dao.EmailHistoryMapper;
import com.thesis.village.model.BusinessException;
import com.thesis.village.model.ResponseResult;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.email.EmailHistory;
import com.thesis.village.model.email.SendEmailDTO;
import com.thesis.village.model.email.UserEmailConfig;
import com.thesis.village.service.EmailService;
import com.thesis.village.service.FileStorageService;
import com.thesis.village.service.impl.EmailServiceImpl;
import com.thesis.village.utils.AuthCodeCrypto;
import com.thesis.village.utils.JavaMailUntil;
import com.thesis.village.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yh
 */

@RestController
@RequestMapping("/email")
public class EmailController {
    
    @Autowired
    private EmailService emailService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private EmailHistoryMapper emailHistoryMapper;
    private String BaseDir = "E:/1/thesis_test/village_test";
    
    @GetMapping("/users")
    public ResponseResult<List<User>> getUsers() {
        return ResponseResult.success(emailService.getAllEmail());
    }
    
    @GetMapping("/history")
    public ResponseResult<List<EmailHistory>> getHistory() {
        return ResponseResult.success(emailHistoryMapper.selectList(null));
    }
    
    @GetMapping("/test")
    public void test() throws Exception {
        String ulcqtagjkcaybccc = AuthCodeCrypto.encrypt("ulcqtagjkcaybccc");
        System.out.println(ulcqtagjkcaybccc);
        String decrypt = AuthCodeCrypto.decrypt(ulcqtagjkcaybccc);
        System.out.println(decrypt);
        return ;
    }
    
    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<Void> sendEmail(@RequestParam String from,
                                    @RequestParam List<String> to,
                                    @RequestParam String subject,
                                    @RequestParam String content,
                                    @RequestPart(value = "attachments",required = false) List<MultipartFile> attachments) throws Exception {

        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Number) map.get("id")).longValue();
        UserEmailConfig userEmailConfig = emailService.getAuthCode(from, id);
        if(userEmailConfig == null){ throw new  BusinessException(1001,"先配置授权码"); }
        Session session = JavaMailUntil.createSession(from,userEmailConfig.getAuthCode());
        
        try {
            List<String> storeEmailFile = fileStorageService.storeEmailFile(attachments);
            List<String> fileNames = storeEmailFile.stream().map(s -> BaseDir + s).collect(Collectors.toList());
            List<String> saveFileNames = storeEmailFile.stream().map(s -> "/api" + s).collect(Collectors.toList());
            //	创建邮件对象
            MimeMessage message = new MimeMessage(session);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] addresses = to.stream()
                    .map(email -> {
                        try {
                            return new InternetAddress(email.trim());
                        } catch (AddressException e) {
                            return null; // 返回null用于后续过滤
                        }
                    })
                    .filter(Objects::nonNull)
                    .toArray(InternetAddress[]::new);
            message.setRecipients(Message.RecipientType.TO,addresses);

            BodyPart textPart = new MimeBodyPart();
            textPart.setContent(content,"text/html;charset=utf-8");

            // 创建包含正文和附件的复合邮件体
            MimeMultipart multipart = new MimeMultipart("mixed"); // 支持嵌套结构
            multipart.addBodyPart(textPart);

            // 传入的是List<String> 需要去转化为 List<File>
            List<File> attachmentsList = fileNames.stream()
                    .map(File::new)
                    .collect(Collectors.toList());


            for (File file : attachmentsList) {
                MimeBodyPart filePart = new MimeBodyPart();
                try {
                    // 自动识别MIME类型（需引入Apache Tika依赖）
                    String mimeType = Files.probeContentType(file.toPath());
                    DataSource source = new FileDataSource(file);
                    filePart.setDataHandler(new DataHandler(source));

                    // 解决中文文件名乱码问题
                    String encodedFileName = MimeUtility.encodeText(file.getName());
                    filePart.setFileName(encodedFileName);

                    multipart.addBodyPart(filePart);
                } catch (IOException e) {
                    throw new BusinessException(1002, "文件读取失败: " + file.getName());
                }
            }
            
            //	将邮件装入信封
            message.setContent(multipart);


            ObjectMapper objectMapper = new ObjectMapper();
            String jsonFiles = objectMapper.writeValueAsString(saveFileNames);

            ObjectMapper toMapper = new ObjectMapper();
            String jsonto = toMapper.writeValueAsString(to);
            
            Transport.send(message);
            EmailHistory emailHistory = new EmailHistory().
                    setSenderEmail(from).
                    setReceiverEmail(jsonto).
                    setSendTime(LocalDateTime.now()).
                    setSubject(subject).
                    setContent(content).
                    setCreatedAt(LocalDateTime.now()).
                    setFiles(jsonFiles);
            emailHistoryMapper.insert(emailHistory);
            return ResponseResult.success("邮件发送成功");
        } catch (IOException e) {
            throw new BusinessException(1001,"文件上传失败");
        }
    }
    
    @GetMapping("/available")
    public ResponseResult<List<String>> getAvailableEmails() {
        return ResponseResult.success(emailService.getAllAvailableEmails());
    }
}
