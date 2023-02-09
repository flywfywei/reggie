package com.wfy.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地址簿实体
 * @author wfy
 * @version 1.0
 */
@Data
public class AddressBook {
    private static final Long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private String consignee;//收货人
    private Integer sex;//性别 0:女，1:男
    private String phone;
    private String provinceCode;//省级区划编号
    private String province_name;//省级名称
    private String cityCode;//市级区划编号
    private String cityName;//市级名称
    private String districtCode;//区级区划编号
    private String districtName;//区级名称
    private String detail;//详细地址
    private String label;//标签
    private Integer isDefault;//默认 0:否，1:是
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
