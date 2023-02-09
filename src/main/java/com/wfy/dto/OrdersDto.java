package com.wfy.dto;

import com.wfy.domain.OrderDetail;
import com.wfy.domain.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wfy
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetails;
    private Integer sumNum;
}
