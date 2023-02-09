package com.wfy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wfy.domain.Category;
import com.wfy.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wfy
 * @version 1.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
