package com.wfy.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜品口味关系实体
 * @author wfy
 * @version 1.0
 */
@Data
@EqualsAndHashCode
public class DishFlavor implements Serializable {
    private static final Long serialVersionUID=1L;
    private Long id;
    private Long dishId;
    private String name;
    private String value;
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
