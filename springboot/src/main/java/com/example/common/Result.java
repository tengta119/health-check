package com.example.common;

public class Result {

    private String code;
    private String message;
    private Object data;

    public Result() {
    }

    public static Result success() {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("请求成功");
        return result;
    }

    public static Result success(Object data) {
        Result result = success();
        result.setData(data);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode("500");
        result.setMessage("请求失败");
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode("500");
        result.setMessage(message);
        return result;
    }

    public static Result error(String code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
