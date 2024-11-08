package com.example.exception;

import com.example.common.enums.ResultCodeEnum;

public class CustomException extends RuntimeException {
    private String code;
    private String message;

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException(String message) {
        this.code = "500";
        this.message = message;
    }

    public CustomException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
