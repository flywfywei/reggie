package com.wfy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wfy.domain.DishFlavor;

import java.util.List;

/**
 * @author wfy
 * @version 1.0
 */
public interface DishFlavorService extends IService<DishFlavor> {
    List<DishFlavor> removeDuplication(List<DishFlavor> flavors);
}
