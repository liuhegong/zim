package com.asm.zim.server.service.impl;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.constants.Result;
import com.asm.zim.server.dao.AccountDao;
import com.asm.zim.server.dao.PersonDao;
import com.asm.zim.server.entry.Account;
import com.asm.zim.server.entry.Person;
import com.asm.zim.server.service.AccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : azhao
 * @description
 */
@Service
public class AccountServiceImpl implements AccountService {
	private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private PersonDao personDao;
	
	@Override
	public Account getAccount(String username, String pwd) {
		QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", username);
		queryWrapper.eq("pwd", pwd);
		return accountDao.selectOne(queryWrapper);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result<String> register(Account account) {
		QueryWrapper<Account> wrapper = new QueryWrapper<>();
		wrapper.eq("username", account.getUsername());
		Integer count = accountDao.selectCount(wrapper);
		if (count > 0) {
			return new Result<String>().failure(account.getUsername() + "用户名已存在");
		}
		account.setId(IdUtil.simpleUUID());
		accountDao.insert(account);
		Person person = new Person();
		person.setId(account.getId());
		person.setName(account.getUsername());
		personDao.insert(person);
		logger.info("{} 账户注册成功",account);
		return new Result<String>().success();
	}
}
