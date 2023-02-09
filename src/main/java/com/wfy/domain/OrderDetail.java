package com.wfy.domain;

import lombok.Data;

/**
 * 订单详情实体
 * @author wfy
 * @version 1.0
 */
@Data
public class OrderDetail {
    private static final Long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String image;
    private Long orderId;
    private Long dishId;
    private Long setmealId;
    private String dishFlavor;
    private Integer number;//数量 默认: 1
    private Double amount;//金额
}
