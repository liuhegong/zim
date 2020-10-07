package com.asm.zim.file.server.filter;

import com.alibaba.fastjson.JSON;
import com.asm.zim.common.constants.Result;
import com.asm.zim.common.constants.ResultCode;
import com.asm.zim.common.entry.TokenAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : azhao
 * @description
 */
@Component

public class LoginFilter implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(LoginFilter.class);
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getParameter("token");
		boolean isLogin = true;
		if (StringUtils.isEmpty(token)) {
			Cookie[] cookies = request.getCookies();
			if (cookies == null) {
				isLogin = false;
			} else {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("token")) {
						token = cookie.getValue();
						break;
					}
				}
				if (token == null) {
					isLogin = false;
				}
			}
		}
		if (!isLogin) {
			logger.info("token为空");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter printWriter = response.getWriter();
			Result<String> result = new Result<String>().failure("token为空");
			printWriter.write(JSON.toJSONString(result));
			printWriter.flush();
			printWriter.close();
			return false;
		}
		
		TokenAuth tokenAuth = (TokenAuth) redisTemplate.opsForValue().get(token);
		if (tokenAuth == null) {
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter printWriter = response.getWriter();
			Result<String> result = new Result<String>().failure(ResultCode.UnLogin);
			printWriter.write(JSON.toJSONString(result));
			printWriter.flush();
			printWriter.close();
			return false;
		} else {
			return true;
		}
	}
	
}
