package com.wfy.dto;

import com.wfy.domain.Setmeal;
import com.wfy.domain.SetmealDish;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wfy
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes = new ArrayList<>();
    private String categoryName;//用于前端分页显示套餐分类名称
}
