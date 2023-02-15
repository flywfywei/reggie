package com.wfy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.*;
import com.wfy.dto.DishDto;
import com.wfy.mapper.CategoryMapper;
import com.wfy.mapper.DishMapper;
import com.wfy.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 批量检查菜品的状态是否为起售
     * @param ids
     * @return
     */
    public boolean checkIdsStatus(List<Long> ids) {
        List<Dish> dishList = this.listByIds(ids);
        for (Dish dish : dishList) {
            if (dish.getStatus() == 1){
                return true;
            }
        }
        return false;
    }

    /**
     * 修改菜品对应套餐的状态
     * @param id
     */
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

    /**
     * 获取DtoList(带有口味数据)
     * @param categoryId
     * @return
     */
    public List<DishDto> getDtoList(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        queryWrapper.eq(Dish::getStatus, 1);
        List<Dish> list = list(queryWrapper);
        List<DishDto> dtoList;
        dtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(wrapper);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        return dtoList;
    }
}
