package com.thesis.village.exception;

import com.thesis.village.model.BusinessException;
import com.thesis.village.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

/**
 * @author yh
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler
    @ResponseBody
    public ResponseResult<String> handle(BusinessException e, HandlerMethod handlerMethod) {
        // BusinessException（自定义业务异常）的处理逻辑，比如：记录日志等逻辑。
        return ResponseResult.fail(e.getErrorCode(),e.getErrorMessage(),"");
    }

}
