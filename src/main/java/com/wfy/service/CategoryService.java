package com.wfy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wfy.domain.Category;

/**
 * @author wfy
 * @version 1.0
 */
public interface CategoryService extends IService<Category> {
    boolean remove(Long id);
}
