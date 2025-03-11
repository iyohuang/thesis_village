package com.thesis.village.model.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDTO {
    @NotBlank(message = "发件人不能为空")
    private String from;

    @NotBlank(message = "收件人不能为空")
    @Email(message = "邮箱格式不正确")
    private String to;

    @NotBlank(message = "邮件主题不能为空")
    private String subject;

    @NotBlank(message = "邮件内容不能为空")
    private String content;

    @NotNull(message = "附件不能为空")
    private List<MultipartFile> attachments;
}
