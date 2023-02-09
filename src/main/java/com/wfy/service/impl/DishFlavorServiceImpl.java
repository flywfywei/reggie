package com.wfy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.DishFlavor;
import com.wfy.mapper.DishFlavorMapper;
import com.wfy.service.DishFlavorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    /**
     * 口味去重 -- 根据 dishFlavor的name
     * @param flavors
     * @return
     */
    @Override
    public List<DishFlavor> removeDuplication(List<DishFlavor> flavors) {
        for (int i = 0; i < flavors.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (flavors.get(i).getName().equals(flavors.get(j).getName())) {
                    flavors.remove(i);
                }
            }
        }
        return flavors;
    }
}
