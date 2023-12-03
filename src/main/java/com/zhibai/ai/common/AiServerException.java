package com.zhibai.ai.common;

import lombok.Data;

import java.text.MessageFormat;

/**
 * @Author xunbai
 * @Date 2023-05-13 17:01
 * @description
 **/
@Data
public class AiServerException extends RuntimeException{

    private String code;

    private String message;

    public AiServerException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public AiServerException(AiServerExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public AiServerException(AiServerExceptionEnum exceptionEnum, Object... args) {
        this.code = exceptionEnum.getCode();
        if (args.length == 0){
            this.message = exceptionEnum.getMessage();
        } else {
            String messageTemplate = exceptionEnum.getMessage();
            this.message = MessageFormat.format(messageTemplate, args);
        }
    }
}
