package com.wfy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.AddressBook;
import com.wfy.domain.OrderDetail;
import com.wfy.domain.Orders;
import com.wfy.domain.ShoppingCart;
import com.wfy.dto.OrdersDto;
import com.wfy.mapper.OrdersMapper;
import com.wfy.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 提交订单
     * @param orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        //保存订单
        //得到购物车集合
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,orders.getUserId());
        List<ShoppingCart> cartList = shoppingCartService.list(queryWrapper);
        //清空购物车
        shoppingCartService.removeBatchByIds(cartList);
        //获取收获人的信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        Double amount = .0;
        for (ShoppingCart shoppingCart : cartList) {
            amount += shoppingCart.getAmount();
        }
        orders.setAmount(amount);
        orders.setCheckoutTime(LocalDateTime.now());
        save(orders);

        //保存订单明细
        //获取订单号
        Long id = orders.getId();
        //将购物车集合处理转化成订单明细集合
        List<OrderDetail> orderDetails = cartList.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            //属性拷贝
            BeanUtils.copyProperties(item,orderDetail,"id");//userId,createTime
            orderDetail.setOrderId(id);
            return orderDetail;
        }).collect(Collectors.toList());
        //批量保存
        orderDetailService.saveBatch(orderDetails);
    }

    /**
     * 查询page并转化为dtoPage
     * @param page
     * @return
     */
    @Override
    public Page<OrdersDto> dtoPage(Page<Orders> page) {
        Page<OrdersDto> dtoPage = new Page<>();
        //转化为 OrdersDto 分页
        List<Orders> orders = page.getRecords();
        List<OrdersDto> ordersDtos = orders.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            //得到订单详细
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId,item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(wrapper);
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(page,dtoPage,"records");
        dtoPage.setRecords(ordersDtos);
        return dtoPage;
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void again(Long id) {
        Orders one = getById(id);
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);
        List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
        Orders orders = new Orders();
        BeanUtils.copyProperties(one,orders,"id");
        orders.setStatus(2);//
        save(orders);
        orderDetails = orderDetails.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item,orderDetail,"id");
            orderDetail.setOrderId(orders.getId());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(orderDetails);
    }
}
