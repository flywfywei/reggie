package com.wfy.common;

import com.wfy.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 * @author wfy
 * @version 1.0
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * SQL完整性约束违反异常 -- 处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<Object> exceptionHandler(SQLIntegrityConstraintViolationException e){
        if (e.getMessage().contains("Duplicate entry")) {//重复录入
            String[] s = e.getMessage().split(" ");
            return R.error(s[2] + "已存在");
        }
        return R.error("未知异常错误");
    }

    /**
     * 自定义异常 -- 处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<Object> exceptionHandler(CustomException e){
        return R.error(e.getMessage());
    }
}
