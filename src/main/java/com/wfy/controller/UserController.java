package com.wfy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wfy.common.R;
import com.wfy.domain.User;
import com.wfy.exception.CustomException;
import com.wfy.service.UserService;
import com.wfy.utils.SMSUtils;
import com.wfy.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 * @author wfy
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 获取验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> getValidateCode(@RequestBody User user, HttpSession session){
        int code = ValidateCodeUtils.generateValidateCode(6);
        log.info("{}的验证码是：{}", user.getPhone(), code);
        //保存在session中，默认3分钟
//        session.setAttribute(user.getPhone(), String.valueOf(code));
        //保存在redis中
        stringRedisTemplate.opsForValue().set(user.getPhone(), String.valueOf(code),5, TimeUnit.MINUTES);
        try {
            //为手机发送验证码
//            SMSUtils.sendMessage(user.getPhone(), code);
        } catch (Exception e){
            throw new CustomException("验证码发送失败");
        }
        return R.success("验证码已发送:"+code);
    }

    /**
     * 用户注册登录
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> user, HttpSession session){
        String phone = user.get("phone");
        String code = user.get("code");
        //比较用户验证码
        //从session中获取正确的验证码
//        String rightCode = (String) session.getAttribute(phone);
        //从 redis 中获取正确的验证码
        String rightCode = stringRedisTemplate.opsForValue().get(phone);
        if (rightCode != null && rightCode.equals(code)) {
            //删除 session 中的验证码
//            session.removeAttribute(phone);
            //删除 redis 中的验证码
            stringRedisTemplate.delete(phone);
            //验证成功，判断是否存在该用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User entity = userService.getOne(queryWrapper);
            if (entity == null) {
                entity = new User();
                entity.setPhone(phone);
                entity.setStatus(1);
                userService.save(entity);
            }
            session.setAttribute("user", entity.getId());
            return R.success(entity);
        }
        return R.error("登录失败");
    }

    /**
     * 用户退出登录
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出登录成功");
    }
}
