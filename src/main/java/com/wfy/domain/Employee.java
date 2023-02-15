package com.wfy.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 * @author wfy
 * @version 1.0
 */
@Data
public class Employee implements Serializable {
    private static final Long serialVersionUID=1L;
    private Long id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String sex;//性别 0:女，1:男
    private String idNumber;
    private Integer status;//状态(默认:1) 0:禁用，1:正常
    @TableField(fill=FieldFill.INSERT)//插入时填入字段
    private LocalDateTime createTime;
    @TableField(fill=FieldFill.INSERT_UPDATE)//插入和更新时填入字段
    private LocalDateTime updateTime;
    @TableField(fill= FieldFill.INSERT)//插入时填入字段
    private Long createUser;
    @TableField(fill=FieldFill.INSERT_UPDATE)//插入和更新时填入字段
    private Long updateUser;
}
