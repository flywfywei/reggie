package com.wfy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfy.domain.Setmeal;
import com.wfy.dto.SetmealDto;

import java.util.List;

/**
 * @author wfy
 * @version 1.0
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 检查套餐是否正在起售
     * @param ids
     * @return
     */
    boolean checkIdsStatus(List<Long> ids);
}
