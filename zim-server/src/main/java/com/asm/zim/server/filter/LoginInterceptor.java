package com.asm.zim.server.filter;

import com.alibaba.fastjson.JSON;
import com.asm.zim.common.constants.Result;
import com.asm.zim.common.constants.ResultCode;
import com.asm.zim.server.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author : azhao
 * @description
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = tokenService.getToken(request);
		boolean hasLogin = tokenService.verifyToken(token);
		if (!hasLogin) {
			logger.info("未登录~");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter printWriter = response.getWriter();
			Result<String> result = new Result<String>().failure(ResultCode.UnLogin);
			printWriter.write(JSON.toJSONString(result));
			printWriter.flush();
			printWriter.close();
			return false;
		}
		
		return true;
	}
}
