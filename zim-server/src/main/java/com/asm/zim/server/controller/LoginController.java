package com.asm.zim.server.controller;

import com.asm.zim.common.constants.MessageCode;
import com.asm.zim.common.constants.Result;
import com.asm.zim.common.constants.ResultCode;
import com.asm.zim.server.core.chat.SystemChatService;
import com.asm.zim.server.entry.Account;
import com.asm.zim.server.service.AccountService;
import com.asm.zim.server.service.LoginService;
import com.asm.zim.server.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Map;

/**
 * @author : azhao
 */
@RestController
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private AccountService accountService;
	@Autowired
	private SystemChatService systemChatService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value = "login")
	public Result<Map<String, String>> login(@RequestParam(value = "username") String username,
	                                         @RequestParam(value = "pwd") String pwd) {
		return loginService.login(username, pwd);
		
	}
	
	@RequestMapping(value = "hasLogin")
	public Result<String> hasLogin() {
		String personId = tokenService.getPersonId();
		if (personId != null) {
			return new Result<String>().success();
		}
		return new Result<String>().failure(ResultCode.UnLogin);
	}
	
	@RequestMapping(value = "register")
	public Result register(@RequestParam(value = "username") String username,
	                       @RequestParam(value = "pwd") String pwd) {
		Account account = new Account(username, pwd);
		return accountService.register(account);
	}
	
	@RequestMapping(value = "loginOut")
	public Result<String> loginOut() {
		String token = tokenService.getToken();
		return loginService.loginOut(token, MessageCode.NormalLoginOut);
	}
}
