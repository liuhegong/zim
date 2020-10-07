package com.asm.zim.server.service.impl;

import com.asm.zim.common.constants.MessageCode;
import com.asm.zim.common.constants.Result;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.core.chat.SystemChatService;
import com.asm.zim.server.entry.Account;
import com.asm.zim.server.service.AccountService;
import com.asm.zim.server.service.LoginService;
import com.asm.zim.server.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : azhao
 * @description
 */
@Service
public class LoginServiceImpl implements LoginService {
	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	@Autowired
	private AccountService accountService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private SystemChatService systemChatService;
	
	@Override
	public Result<Map<String, String>> login(String username, String pwd) {
		Account account = accountService.getAccount(username, pwd);
		if (account == null) {
			return new Result<Map<String, String>>().failure();
		}
		String token = tokenService.getToken(account.getId());
		if (token != null) {
			//如果已经登录先退出 但不通知下线
			tokenService.removeToken(token);
			systemChatService.loginOut(token, MessageCode.ConflictLoginOut);
		}
		token = tokenService.createToken(account.getId());
		tokenService.setCookie(token);
		Result<Map<String, String>> result = new Result<>();
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("token", token);
		resultMap.put("personId", account.getId());
		resultMap.put("expireTime", String.valueOf(Constants.SESSION_EXPIRE_TIME));
		result.setData(resultMap);
		result.setMessage("登录成功");
		logger.debug("登录成功 token{}", token);
		return result;
	}
	
	@Override
	public Result<String> loginOut(String token, int messageCode) {
		logger.info("loginOut token {}", token);
		String personId = tokenService.getPersonId(token);
		systemChatService.systemLineMessage(personId, null, false);
		systemChatService.loginOut(token, messageCode);
		tokenService.removeToken(token);
		logger.info("退出 token {}", token);
		return null;
	}
}
