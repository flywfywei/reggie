package com.wfy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.User;
import com.wfy.mapper.UserMapper;
import com.wfy.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
