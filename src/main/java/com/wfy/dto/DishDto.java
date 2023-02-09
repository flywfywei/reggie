package com.wfy.dto;

import com.wfy.domain.Dish;
import com.wfy.domain.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Dish数据传输对象
 * @author wfy
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;//用于分页显示菜品分类名称
    private Integer copies;//用于前台套餐展示
}
