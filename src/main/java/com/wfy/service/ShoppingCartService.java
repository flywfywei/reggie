package com.wfy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wfy.domain.ShoppingCart;

/**
 * @author wfy
 * @version 1.0
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 添加或减少菜品或套餐到购物车
     * @param shoppingCart
     * @param isAdd
     */
    void addOrSub(ShoppingCart shoppingCart, boolean isAdd);

}
