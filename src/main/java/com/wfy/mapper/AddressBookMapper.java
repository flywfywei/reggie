package com.wfy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wfy.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wfy
 * @version 1.0
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
