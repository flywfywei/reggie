package com.wfy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wfy.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wfy
 * @version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
