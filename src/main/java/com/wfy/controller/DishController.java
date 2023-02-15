package com.wfy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfy.domain.*;
import com.wfy.dto.DishDto;
import com.wfy.common.R;
import com.wfy.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wfy
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 根据菜品分类id得到list
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(@RequestParam Long categoryId){
        String key = "dish_" + categoryId;
        List<DishDto> dtoList;
        //检查redis中是否存在
        dtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        //存在直接从redis中返回数据
        if (dtoList != null) {
            return R.success(dtoList);
        }
        //不存在时从数据库查询
        dtoList = dishService.getDtoList(categoryId);
        //将查询结果保存在redis中
        redisTemplate.opsForValue().set(key, dtoList, 60, TimeUnit.MINUTES);
        return R.success(dtoList);
    }

    /**
     * 菜品管理页面分页显示
     * @param pageNo
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> getDishPage(@RequestParam("page") Integer pageNo, Integer pageSize, String name){
        Page<Dish> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //条件查询
        queryWrapper.like(StringUtils.hasLength(name), Dish::getName, name);
        //排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        dishService.page(page, queryWrapper);
        //分类名称显示
        Page<DishDto> dtoPage = new Page<>();
        List<Dish> dishList = page.getRecords();
        List<DishDto> dtoList = new ArrayList<>();
        for (Dish dish : dishList) {
            DishDto dishDto = new DishDto();
            //对象拷贝，拷贝属性
            BeanUtils.copyProperties(dish, dishDto);
            //查询 categoryName 用于分页显示菜品分类名称
            LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Category::getId, dishDto.getCategoryId());
            String categoryName = categoryService.getOne(wrapper).getName();
            dishDto.setCategoryName(categoryName);
            dtoList.add(dishDto);
        }
        //对象拷贝，拷贝属性
        BeanUtils.copyProperties(page, dtoPage, "records");
        dtoPage.setRecords(dtoList);
        return R.success(dtoPage);
    }

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
        String key = "dish_" + dishDto.getCategoryId();
        List<DishFlavor> flavors = dishFlavorService.removeDuplication(dishDto.getFlavors());
        dishService.save(dishDto);
        //为菜品口味联系对象赋菜品id值并保存
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavorService.save(flavor);
        }
        //删除redis缓存
        redisTemplate.delete(key);
        return R.success("菜品保存成功");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        if (!dishService.checkIdsStatus(ids)) {
            dishService.removeBatchByIds(ids);
            return R.success("删除成功");
        }
        //删除redis缓存
        Set<Object> keys = redisTemplate.keys("dish_*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
        return R.error("在售菜品不可被删除");
    }

    /**
     * 批量启用、禁用状态
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status){
        Dish dish = new Dish();//定义在循环外，节省空间
        for (Long id : ids) {
            //禁售时，含有该菜品的套餐同时禁售
            if (status == 0){
                dishService.updateDishSetmealStatus(id);
            }
            dish.setId(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        //删除redis缓存
        Set<Object> keys = redisTemplate.keys("dish_*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
        return R.success("删除成功");
    }

    /**
     * 修改回显菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);//属性拷贝
        //条件构造器
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    @Transactional
    public R<String> updateDish(@RequestBody DishDto dishDto){
        List<DishFlavor> flavors = dishFlavorService.removeDuplication(dishDto.getFlavors());
        dishService.updateById(dishDto);
        //删除数据库中原有菜品口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //为菜品口味联系对象赋菜品id值并保存
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavorService.save(flavor);
        }
        //删除redis缓存
        String key = "dish_" + dishDto.getCategoryId();
        redisTemplate.delete(key);
        return R.success("菜品修改成功");
    }
}
