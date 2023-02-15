package com.wfy.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车实体
 * @author wfy
 * @version 1.0
 */
@Data
public class ShoppingCart implements Serializable {
    private static final Long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String image;
    private Long userId;
    private Long dishId;
    private Long setmealId;
    private String dishFlavor;
    private Integer number;//数量 默认: 1
    private Double amount;//金额
    private LocalDateTime createTime;
}
