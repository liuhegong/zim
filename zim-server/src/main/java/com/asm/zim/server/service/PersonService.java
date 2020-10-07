package com.asm.zim.server.service;

import com.asm.zim.server.entry.Person;

/**
 * @author : azhao
 * @description
 */
public interface PersonService {
	/**
	 * 通过id 查询
	 *
	 * @param id
	 * @return
	 */
	Person findPerson(String id);
	
	Person findPersonByUsername(String username);
	
	void updatePersonById(Person person);
}
