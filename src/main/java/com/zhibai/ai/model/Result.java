package com.zhibai.ai.model;

import com.zhibai.ai.common.AiServerExceptionEnum;
import lombok.Data;

/**
 * @Author xunbai
 * @Date 2023-05-13 17:00
 * @description
 **/
@Data
public class Result<T> {

    private String code;

    private String message;

    private boolean success;

    private T data;

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.code = "200";
        result.success = true;
        result.data = data;
        return result;
    }

    public static <T> Result<T> success(String message, T data){
        Result<T> result = new Result<>();
        result.code = "200";
        result.message = message;
        result.success = true;
        result.data = data;
        return result;
    }

    public static <T> Result<T> success(){
        Result<T> result = new Result<>();
        result.code = "200";
        result.success = true;
        return result;
    }

    public static <T> Result<T> successOfMessage(String message){
        Result<T> result = new Result<>();
        result.code = "200";
        result.message = message;
        result.success = true;
        return result;
    }

    public static <T> Result<T> failed(String code, String message){
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.success = false;
        return result;
    }

    public static <T> Result<T> failed(AiServerExceptionEnum exceptionEnum){
        Result<T> result = new Result<>();
        result.code = exceptionEnum.getCode();
        result.message = exceptionEnum.getMessage();
        result.success = false;
        return result;
    }

    public static <T> Result<T> validationFailed(){
        Result<T> result = new Result<>();
        result.code = AiServerExceptionEnum.VALIDATION_ERROR.getCode();
        result.message = AiServerExceptionEnum.VALIDATION_ERROR.getMessage();
        result.success = false;
        return result;
    }
}
