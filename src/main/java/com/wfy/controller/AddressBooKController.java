package com.wfy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wfy.common.R;
import com.wfy.domain.AddressBook;
import com.wfy.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 地址簿管理
 * @author wfy
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBooKController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 获取用户默认的地址
     * @param session
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault(HttpSession session){
        Long user = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, user)
                        .eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }

    /**
     * 当前登录用户的地址集合
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> getList(HttpSession session){
        Long user = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, user);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook, HttpSession session){
        Long user = (Long) session.getAttribute("user");
        addressBook.setUserId(user);
        addressBookService.save(addressBook);
        return R.success("地址保存成功");
    }

    /**
     * 删除地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long ids){
        addressBookService.removeById(ids);
        return R.success("地址保存成功");
    }

    /**
     * 地址详细
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> getOne(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    /**
     * 设置地址为默认地址
     * @param addressBook
     * @param session
     * @return
     */
    @PutMapping("/default")
    public R putDefault(@RequestBody AddressBook addressBook,HttpSession session){
        addressBook.setIsDefault(1);
        Long user = (Long) session.getAttribute("user");
        //清除
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, user)
                .set(AddressBook::getIsDefault, 1);
        addressBookService.update(updateWrapper);
        //重新保存
        addressBookService.updateById(addressBook);
        return R.success("已将该地址设为默认");
    }
}
