package com.asm.zim.server.dao;

import com.asm.zim.server.entry.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author : azhao
 * @description
 */
@Repository
@Mapper
public interface AccountDao extends BaseMapper<Account> {
}
