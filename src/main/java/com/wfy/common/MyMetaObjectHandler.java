package com.wfy.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 自定义的元数据对象处理器
 * @author wfy
 * @version 1.0
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    HttpServletRequest httpServletRequest;

    /**
     * 执行插入操作时，自动为指定字段赋值
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        metaObject.setValue("createTime", now);
        metaObject.setValue("updateTime", now);
        Long userInfo = (Long) httpServletRequest.getSession().getAttribute("userInfo");
        if (userInfo != null) {
            metaObject.setValue("createUser", userInfo);
            metaObject.setValue("updateUser", userInfo);
        }else {
            Long user = (Long) httpServletRequest.getSession().getAttribute("user");
            metaObject.setValue("createUser", user);
            metaObject.setValue("updateUser", user);
        }
    }

    /**
     * 执行更新操作时，自动为指定字段赋值
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        metaObject.setValue("updateTime", now);
        Long userInfo = (Long) httpServletRequest.getSession().getAttribute("userInfo");
        if (userInfo != null) {
            metaObject.setValue("updateUser", userInfo);
        }else {
            Long user = (Long) httpServletRequest.getSession().getAttribute("user");
            metaObject.setValue("updateUser", user);
        }
    }
}
