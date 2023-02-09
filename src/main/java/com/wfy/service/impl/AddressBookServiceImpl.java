package com.wfy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfy.domain.AddressBook;
import com.wfy.mapper.AddressBookMapper;
import com.wfy.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author wfy
 * @version 1.0
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
