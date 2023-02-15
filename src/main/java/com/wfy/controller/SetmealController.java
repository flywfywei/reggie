package com.wfy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfy.common.R;
import com.wfy.domain.*;
import com.wfy.dto.DishDto;
import com.wfy.dto.SetmealDto;
import com.wfy.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wfy
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;

    /**
     * 展示套餐的菜品列表
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable("id") String id){
        //得到套餐的菜品集合
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        //将菜品集合遍历处理得到 DishDto 集合，便于前台展示
        List<DishDto> dishDtos = list.stream().map((item)->{
            //根据菜品id得到菜品的详细信息（主要是图片资源）
            DishDto dishDto = new DishDto();
            Long dishId = Long.valueOf(item.getDishId());
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDto);
            //将套餐对应的菜品的份数进行拷贝
            dishDto.setCopies(item.getCopies());
            //得到菜品的口味
            /*LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(flavors);*/
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }

    /**
     * 根据分类id获取套餐列表
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId")
    public R<List<Setmeal>> list(Setmeal setmeal){
        List<Setmeal> list = setmealService.list(new LambdaQueryWrapper<>(setmeal));
        return R.success(list);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        if (!setmealService.checkIdsStatus(ids)){
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Setmeal::getId, ids);
            setmealService.remove(queryWrapper);
            //删除套餐关系的菜品
            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SetmealDish::getSetmealId, ids);
            setmealDishService.remove(wrapper);
            return R.success("删除成功");
        }
        return R.error("删除失败，存在在售套餐");
    }

    /**
     * 修改套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    @Transactional
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto){
        //保存套餐信息
        setmealService.updateById(setmealDto);
        //删除原有套餐菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //添加新的套餐菜品
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().peek((item) -> item.setSetmealId(String.valueOf(setmealDto.getId()))).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
        return R.success("修改成功");
    }

    /**
     * 获取一个套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmeal(@PathVariable("id") Long id) {
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //条件构造器
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id.toString());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        //属性拷贝
        BeanUtils.copyProperties(setmeal, setmealDto);
        return R.success(setmealDto);
    }

    /**
     * 批量修改状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> updateStatus(@PathVariable("status") Integer status, Long[] ids){
        List<Setmeal> setmeals = Arrays.stream(ids).map((item) -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(item);
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(setmeals);
        return R.success("状态修改完成");
    }

    /**
     * 保存套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @Transactional
    @CacheEvict(value = "setmealCache", key = "#setmealDto.categoryId")
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.save(setmealDto);
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().peek((item) -> item.setSetmealId(String.valueOf(setmealDto.getId()))).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
        return R.success("保存成功");
    }

    /**
     * 套餐获取分页显示
     *
     * @param pageNo
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam("page") Integer pageNo, Integer pageSize, String name) {
        Page<SetmealDto> dtoPage = new Page<>();
        //分页构造器
        Page<Setmeal> page = new Page<>(pageNo, pageSize);
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name), Setmeal::getName, name);
        //排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(page, queryWrapper);
        //属性拷贝
        BeanUtils.copyProperties(page, dtoPage, "records");
        List<Setmeal> list = page.getRecords();
        List<SetmealDto> dtoList = list.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //属性拷贝
            BeanUtils.copyProperties(item, setmealDto);
            //查询套餐分类名称
            Category category = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        return R.success(dtoPage);
    }
}
