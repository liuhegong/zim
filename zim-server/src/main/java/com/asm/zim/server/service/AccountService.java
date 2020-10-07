package com.asm.zim.server.service;

import com.asm.zim.common.constants.Result;
import com.asm.zim.server.entry.Account;

/**
 * @author : azhao
 * @description
 */
public interface AccountService {
	Account getAccount(String username, String pwd);
	
	Result<String> register(Account account);
	
}
