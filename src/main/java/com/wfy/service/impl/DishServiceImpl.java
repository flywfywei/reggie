package com.wfy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.Category;
import com.wfy.domain.Dish;
import com.wfy.domain.Setmeal;
import com.wfy.domain.SetmealDish;
import com.wfy.mapper.CategoryMapper;
import com.wfy.mapper.DishMapper;
import com.wfy.service.CategoryService;
import com.wfy.service.DishService;
import com.wfy.service.SetmealDishService;
import com.wfy.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    public boolean checkIdsStatus(List<Long> ids) {
        List<Dish> dishList = this.listByIds(ids);
        for (Dish dish : dishList) {
            if (dish.getStatus() == 1){
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateDishSetmealStatus(Long id) {
        //根据当前菜品id从套餐菜品关系表中得到对象
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getDishId, id.toString());
        SetmealDish setmealDish = setmealDishService.getOne(queryWrapper);
        //如果对象不为空
        if (setmealDish != null) {
            //说明存在使用该菜品的套餐，通过套餐id修改套餐状态
            String setmealId = setmealDish.getSetmealId();
            Setmeal setmeal = new Setmeal();
            //设置套餐id
            setmeal.setId(Long.valueOf(setmealId));
            //设置套餐状态
            setmeal.setStatus(0);
            setmealService.updateById(setmeal);
        }
    }
}
