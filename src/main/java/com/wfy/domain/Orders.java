package com.wfy.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单实体
 * @author wfy
 * @version 1.0
 */
@Data
public class Orders implements Serializable {
    private static final Long serialVersionUID=1L;
    private Long id;
    private String number;
    private Integer status;//订单状态 1:待付款，2:待派送，3:已派送，4:已完成，5:已取消
    private Long userId;//账号使用人id
    private Long addressBookId;//地址簿
    private LocalDateTime orderTime;//下单时间
    private LocalDateTime checkoutTime;//结账时间
    private Integer payMethod;//支付方式 1:微信，2:支付宝
    private Double amount;//实收金额
    private String remark;//备注
    private String phone;
    private String address;
    private String userName;//账号使用人名称
    private String consignee;//收货人
}
