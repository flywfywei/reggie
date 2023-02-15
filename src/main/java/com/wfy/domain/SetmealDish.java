package com.wfy.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 套餐菜品关系实体
 * @author wfy
 * @version 1.0
 */
@Data
public class SetmealDish implements Serializable {
    private static final Long serialVersionUID=1L;
    private Long id;
    private String setmealId;
    private String dishId;
    private String name;
    private Double price;
    private Integer copies;//份数
    private Integer sort;
    @TableField(fill= FieldFill.INSERT)//插入时填入字段
    private LocalDateTime createTime;
    @TableField(fill=FieldFill.INSERT_UPDATE)//插入和更新时填入字段
    private LocalDateTime updateTime;
    @TableField(fill= FieldFill.INSERT)//插入时填入字段
    private Long createUser;
    @TableField(fill=FieldFill.INSERT_UPDATE)//插入和更新时填入字段
    private Long updateUser;
//    @TableLogic(value = "0", delval = "1")//逻辑删除
    private Integer isDeleted;//是否删除
}
