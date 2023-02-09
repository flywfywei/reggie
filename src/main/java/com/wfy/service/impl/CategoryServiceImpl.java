package com.wfy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.Category;
import com.wfy.domain.Dish;
import com.wfy.domain.Setmeal;
import com.wfy.exception.CustomException;
import com.wfy.mapper.CategoryMapper;
import com.wfy.service.CategoryService;
import com.wfy.service.DishService;
import com.wfy.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setMealService;

    /**
     * 根据业务删除分类
     * @param id
     * @return
     */
    @Override
    public boolean remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = (int) dishService.count(dishQueryWrapper);
        //该分类与菜品存在关联，抛出自定义业务异常
        if (dishCount > 0) {
            throw new CustomException("该分类与菜品存在关联，删除失败");
        }
        LambdaQueryWrapper<Setmeal> setMealQueryWrapper = new LambdaQueryWrapper<>();
        setMealQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setMealCount = (int) setMealService.count(setMealQueryWrapper);
        //该分类与套餐存在关联，抛出自定义业务异常
        if (setMealCount > 0) {
            throw new CustomException("该分类与套餐存在关联，删除失败");
        }
        //执行删除命令
        return this.removeById(id);
    }
}
