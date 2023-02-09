package com.wfy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.OrderDetail;
import com.wfy.mapper.OrderDetailMapper;
import com.wfy.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
