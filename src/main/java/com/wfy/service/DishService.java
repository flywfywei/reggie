package com.wfy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wfy.domain.Dish;

import java.util.List;

/**
 * @author wfy
 * @version 1.0
 */
public interface DishService extends IService<Dish> {
    /**
     * 批量检查菜品的状态是否为起售
     * @param ids
     * @return
     */
    boolean checkIdsStatus(List<Long> ids);

    /**
     * 修改菜品对应套餐的状态
     * @param id
     */
    void updateDishSetmealStatus(Long id);
}
