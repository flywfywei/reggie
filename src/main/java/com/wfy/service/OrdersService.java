package com.wfy.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfy.domain.Orders;
import com.wfy.dto.OrdersDto;

/**
 * @author wfy
 * @version 1.0
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders
     */
    void submit(Orders orders);

    /**
     * 查询page并转化为dtoPage
     * @param page
     * @return
     */
    Page<OrdersDto> dtoPage(Page<Orders> page);

    /**
     * 再来一单
     * @param id
     */
    void again(Long id);
}
