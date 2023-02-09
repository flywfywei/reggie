package com.wfy.domain;

import lombok.Data;

/**
 * 用户实体
 * @author wfy
 * @version 1.0
 */
@Data
public class User {
    private static final Long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String phone;
    private String sex;
    private String idNumber;//身份证号
    private String avatar;//头像
    private Integer status;//状态 0:禁用，1:正常
}
