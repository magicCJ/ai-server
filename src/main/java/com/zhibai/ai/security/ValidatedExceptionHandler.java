package com.zhibai.ai.security;

import com.zhibai.ai.model.Result;
import com.zhibai.ai.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author: kanson
 * @software: IntelliJ IDEA
 * @desc:
 * @version: 1.0
 * @time: 2023/05/28 18:52
 */
@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class ValidatedExceptionHandler {
    /**
     * 处理@Validated参数校验失败异常
     *
     * @param exception 异常类
     * @return 响应
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object exceptionParamHandler(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors.size() > 0) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                    log.warn("Bad Request Parameters: dto entity [{}],field [{}],message [{}]",
                            fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
                    stringBuilder.append(fieldError.getDefaultMessage());
                });
            }
        }
        return Result.failed("400", stringBuilder.toString());
    }

    /**
     * 处理BizException业务异常
     *
     * @param exception 异常类
     * @return 响应
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Object bizExceptionHandler(RuntimeException exception) {
        log.warn("bizExceptionHandler: {}\n{}", exception.getMessage(), StringUtil.getExceptionStackTrace(exception));
        return Result.failed("400", exception.getMessage());
    }

    /**
     * 处理500失败异常
     *
     * @param exception 异常类
     * @return 响应
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Object exceptionServerHandler(Exception exception) {
        log.warn("exceptionServerHandler: {}\n{}", exception.getMessage(), StringUtil.getExceptionStackTrace(exception));
        return Result.failed("500", exception.getMessage());
    }
}
