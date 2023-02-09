package com.wfy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wfy.common.R;
import com.wfy.domain.ShoppingCart;
import com.wfy.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车管理
 * @author wfy
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCarService;

    /**
     * 清空购物车
     * @param session
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> cleanCart(HttpSession session){
        Long user = (Long) session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,user);
        shoppingCarService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

    /**
     * 从购物车中减少食物
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/sub")
    public R<String> subDishToCart(@RequestBody ShoppingCart shoppingCart,HttpSession session){
        Long user = (Long) session.getAttribute("user");
        shoppingCart.setUserId(user);
        shoppingCarService.addOrSub(shoppingCart,false);
        return R.success("减少菜品或套餐成功");
    }

    /**
     * 添加食物到购物车
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/add")
    public R<String> addDishToCart(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        Long user = (Long) session.getAttribute("user");
        shoppingCart.setUserId(user);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCarService.addOrSub(shoppingCart,true);
        return R.success("添加菜品或套餐到购物车成功");
    }

    /**
     * 获取购物车列表
     * @param session
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession session){
        Long user = (Long) session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, user);
        List<ShoppingCart> cartList = shoppingCarService.list(queryWrapper);
        return R.success(cartList);
    }

}
