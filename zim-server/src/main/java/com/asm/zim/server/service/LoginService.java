package com.asm.zim.server.service;

import com.asm.zim.common.constants.Result;

import java.util.Map;

/**
 * @author : azhao
 * @description
 */
public interface LoginService {
	Result<Map<String, String>> login(String username, String pwd,String terminalType);
	
	
	Result<String> loginOut(String token,int messageCode);
}
