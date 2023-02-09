package com.wfy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.ShoppingCart;
import com.wfy.mapper.ShoppingCartMapper;
import com.wfy.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    /**
     * 添加或减少菜品或套餐到购物车
     * @param shoppingCart
     * @param isAdd
     */
    @Override
    public void addOrSub(ShoppingCart shoppingCart, boolean isAdd) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        if (shoppingCart.getDishId() != null)
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        else
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart cart = getOne(queryWrapper);
        if (isAdd) {
            if (cart==null)
                save(shoppingCart);
            else {
                cart.setNumber(cart.getNumber() + 1);
                updateById(cart);
            }
        } else {
            if (cart.getNumber()==1)
                remove(queryWrapper);
            else {
                cart.setNumber(cart.getNumber() - 1);
                updateById(cart);
            }
        }
    }

}
