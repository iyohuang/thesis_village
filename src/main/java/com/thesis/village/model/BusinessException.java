package com.thesis.village.model;

import com.thesis.village.enums.ErrorEnum;
import com.thesis.village.enums.HttpStatusEnum;
import lombok.Getter;
/**
 * @author yh
 */
@Getter
public class BusinessException extends RuntimeException {


    /**
     * 错误码<br>
     * 调用成功时，为 null。<br>
     * 示例：10001
     */
    private final Integer errorCode;

    /**
     * 错误信息<br>
     * 调用成功时，为 null。<br>
     * 示例："验证码无效"
     */
    private final String errorMessage;
    
    public BusinessException(HttpStatusEnum errorEnum) {
        super(String.format("错误码：[%s]，错误信息：[%s]", errorEnum.name(), errorEnum.getMessage()));
        this.errorMessage = errorEnum.getMessage();
        this.errorCode = errorEnum.getCode();
    }


    public BusinessException(Integer errorCode, String errorMessage) {
        super(String.format("错误码：[%s]，错误信息：[%s]", errorCode, errorMessage));
        this.errorMessage = errorMessage;
        this.errorCode = getErrorCode();
    }

}