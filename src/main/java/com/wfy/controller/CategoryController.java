package com.wfy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfy.common.R;
import com.wfy.domain.Category;
import com.wfy.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wfy
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    /**
     * 分类管理页面分页显示
     * @return
     */
    @GetMapping("/page")
    public R<Page<Category>> page(@RequestParam("page") Integer pageNo, Integer pageSize) {
        //构造分页构造器
        Page<Category> page = new Page<>(pageNo, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //构造排序构造器
//        queryWrapper.orderByDesc(Category::getUpdateTime);//根据字段UpdateTime降序
        queryWrapper.orderByAsc(Category::getSort);//根据字段Sort升序
        categoryService.page(page, queryWrapper);
        return R.success(page);
    }

    /**
     * 添加新分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("分类保存成功");
    }

    /**
     * 修改分类
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, category.getId());
        categoryService.update(category, queryWrapper);
        return R.success("修改分类成功");
    }

    /**
     * 删除分类
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id){
        categoryService.remove(id);
        return R.success("删除成功");
    }

    /**
     * 获取分类列表
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getCategoryList(Integer type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null, Category::getType, type);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
