package com.wfy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.Employee;
import com.wfy.mapper.EmployeeMapper;
import com.wfy.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
