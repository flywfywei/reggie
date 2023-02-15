package com.wfy.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 套餐实体
 * @author wfy
 * @version 1.0
 */
@Data
public class Setmeal implements Serializable {
    private static final Long serialVersionUID=1L;
    private Long id;
    private String name;
    private Long categoryId;
    private Double price;
    private String code;
    private String image;
    private String description;//菜品描述信息
    private Integer status;//菜品状态 0:停售，1:起售
    @TableField(fill= FieldFill.INSERT)//插入时填入字段
    private LocalDateTime createTime;
    @TableField(fill=FieldFill.INSERT_UPDATE)//插入和更新时填入字段
    private LocalDateTime updateTime;
    @TableField(fill= FieldFill.INSERT)//插入时填入字段
    private Long createUser;
    @TableField(fill=FieldFill.INSERT_UPDATE)//插入和更新时填入字段
    private Long updateUser;
    @TableLogic(value = "0", delval = "1")//逻辑删除
    private Integer isDeleted;//是否删除
}
