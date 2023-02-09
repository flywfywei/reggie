package com.wfy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wfy.common.R;
import com.wfy.domain.Employee;
import com.wfy.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wfy
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工后台登录
     * @param employee
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<Employee> backendLogin(@RequestBody Employee employee, HttpServletRequest request) {
        //1、将页面提交的密码password进行md5加密处理
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("用户不存在");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if (!password.equals(emp.getPassword())) {
            return R.error("密码错误，请重新输入");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("员工账号已被禁用，请联系管理员解除禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("userInfo", emp.getId());
        log.info("用户：" + emp.getId() + " 登录后台管理系统...");///
        return R.success(emp);
    }

    /**
     * 员工退出后台
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> backendLogout(HttpServletRequest request) {
        Long userInfo = (Long) request.getSession().getAttribute("userInfo");
        log.info("用户：{} 退出后台管理系统...", userInfo);
        request.getSession().removeAttribute("userInfo");
        return R.success("成功退出登录");
    }

    /**
     * 员工管理页面分页显示
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> backendPage(@RequestParam("page") Integer pageNo, Integer pageSize, String name){
        //构造分页构造器
        Page<Employee> page = new Page<>(pageNo, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasLength(name),Employee::getName, name);
        //构造排序构造器
        queryWrapper.orderByDesc(Employee::getUpdateTime);//根据字段降序
        employeeService.page(page, queryWrapper);
        return R.success(page);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee){
        String password = "123456";
        //1. 为新员工设置系统默认密码、创建人与创建时间、修改人与修改时间
        employee.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));

        //2. 增加员工
        employeeService.save(employee);
        return R.success("添加成功");
    }

    /**
     * 通过员工 id 查询员工 -- 修改页面反查详情接口
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> queryEmployeeById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }

    /**
     * 修改员工账号信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateEmployee(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("用户信息修改成功");
    }

}
