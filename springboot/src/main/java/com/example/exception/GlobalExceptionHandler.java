package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.example.controller") //拦截com.example.controller包下的异常
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();

    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json
    public Result error(Exception e) {
        log.error("异常信息" + e);
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e) {
        log.error("异常信息" + e);
        return Result.error(e.getCode(), e.getMessage());
    }
}
