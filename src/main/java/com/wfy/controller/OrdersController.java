package com.wfy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfy.common.R;
import com.wfy.domain.Orders;
import com.wfy.dto.OrdersDto;
import com.wfy.service.OrdersService;
import com.wfy.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理
 * @author wfy
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 再来一单
     * @param orders
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){
        ordersService.again(orders.getId());
        return R.success("订单提交成功");
    }

    /**
     * 生成订单
     * @return
     */
    @PostMapping("/submit")
    public R<String> save(@RequestBody Orders orders, HttpSession session){
        Long user = (Long) session.getAttribute("user");
        orders.setUserId(user);
        orders.setOrderTime(LocalDateTime.now());
        String number = ValidateCodeUtils.generateValidateCode4String(4);
        orders.setNumber(number);
        orders.setStatus(2);
        ordersService.submit(orders);
        return R.success("订单提交成功");
    }

    /**
     * 员工的订单分页显示
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @param pageNo
     * @return
     */
    @GetMapping("/page")
    public R<Page<OrdersDto>> page(Integer pageSize, String number,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                   @RequestParam("page") Integer pageNo){
        Page<Orders> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(number), Orders::getNumber, number);
        queryWrapper.between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime);
        ordersService.page(page,queryWrapper);
        Page<OrdersDto> dtoPage = ordersService.dtoPage(page);
        return R.success(dtoPage);
    }

    /**
     * 修改状态
     * @return
     */
    @PutMapping
    public R<String> updateOrderStatus(@RequestBody Orders orders){
        ordersService.updateById(orders);
        return R.success("状态修改成功");
    }

    /**
     * 用户的订单分页
     * @param pageSize
     * @param pageNo
     * @return
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(HttpSession session, Integer pageSize, @RequestParam("page") Integer pageNo){
        Long user = (Long) session.getAttribute("user");
        Page<Orders> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, user);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(page, queryWrapper);
        Page<OrdersDto> dtoPage = ordersService.dtoPage(page);
        return R.success(dtoPage);
    }
}
