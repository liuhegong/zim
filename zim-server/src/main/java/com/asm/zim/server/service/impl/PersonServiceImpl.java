package com.asm.zim.server.service.impl;

import com.asm.zim.server.dao.AccountDao;
import com.asm.zim.server.dao.PersonDao;
import com.asm.zim.server.entry.Account;
import com.asm.zim.server.entry.Person;
import com.asm.zim.server.service.PersonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : azhao
 * @description
 */
@Service
public class PersonServiceImpl implements PersonService {
	private Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
	
	@Autowired
	private PersonDao personDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Override
	public Person findPerson(String id) {
		if (id == null) {
			return null;
		}
		return personDao.selectById(id);
	}
	
	@Override
	public Person findPersonByUsername(String username) {
		QueryWrapper<Account> wrapper = new QueryWrapper<>();
		wrapper.eq("username", username);
		Account account = accountDao.selectOne(wrapper);
		if (account == null) {
			return null;
		}
		return personDao.selectById(account.getId());
	}
	
	@Override
	public void updatePersonById(Person person) {
		personDao.updateById(person);
	}
}
