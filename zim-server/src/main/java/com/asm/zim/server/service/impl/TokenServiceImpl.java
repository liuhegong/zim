package com.asm.zim.server.service.impl;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.config.yaml.ImConfig;
import com.asm.zim.server.config.yaml.WebsocketConfig;
import com.asm.zim.server.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author : azhao
 * @description
 */
@Service
public class TokenServiceImpl implements TokenService {
	private Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	@Autowired
	private ImConfig imConfig;
	@Autowired
	private WebsocketConfig websocketConfig;
	
	@Override
	public void setCookie(String token) {
		setCookie(token, getResponse());
	}
	
	@Override
	public void refreshByToken(String token) {
		TokenAuth tokenAuth = getTokenAuth(token);
		if (tokenAuth != null) {
			tokenAuth.setLastLoginTime(System.currentTimeMillis());
			redisTemplate.opsForValue().set(token, tokenAuth);
			redisTemplate.expire(token, imConfig.getSessionExpireTime(), TimeUnit.SECONDS);
			redisTemplate.expire(tokenAuth.getPersonId(), imConfig.getSessionExpireTime(), TimeUnit.SECONDS);
		}
		logger.info("token {} 刷新", token);
	}
	
	public void setCookie(String token, HttpServletResponse response) {
		if (response == null) {
			return;
		}
		Cookie cookie = new Cookie(Constants.SESSION_ID, token);
		cookie.setMaxAge(imConfig.getSessionExpireTime());
		response.addCookie(cookie);
	}
	
	@Override
	public String createToken(String personId) {
		String token = IdUtil.fastSimpleUUID();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		TokenAuth tokenAuth = new TokenAuth();
		tokenAuth.setIp(Constants.LOCALHOST);
		tokenAuth.setPersonId(personId);
		tokenAuth.setToken(token);
		tokenAuth.setEquipmentId(Constants.EQUIPMENT_ID);
		tokenAuth.setLastLoginTime(System.currentTimeMillis());
		redisTemplate.opsForValue().set(token, tokenAuth, imConfig.getSessionExpireTime(), TimeUnit.SECONDS);
		redisTemplate.opsForValue().set(personId, token, imConfig.getSessionExpireTime(), TimeUnit.SECONDS);
		logger.info("创建token==>{}", token);
		return token;
	}
	
	@Override
	public boolean verifyToken(String token) {
		if (StringUtils.isEmpty(token)) {
			return false;
		}
		TokenAuth tokenAuth = (TokenAuth) redisTemplate.opsForValue().get(token);
		if (StringUtils.isEmpty(tokenAuth)) {
			logger.info(" token {} 已过期", token);
			return false;
		}
		long lastLoginTime = tokenAuth.getLastLoginTime();
		long now = System.currentTimeMillis();
		int interval = imConfig.getSessionExpireTime() * 1000 * 2 / 3;
		if (now - lastLoginTime > interval) {
			refreshByToken(token);
		}
		logger.info("token {} 验证通过", token);
		return true;
	}
	
	@Override
	public void removeToken(String token) {
		if (token == null) {
			return;
		}
		String personId = getPersonId(token);
		if (personId != null) {
			redisTemplate.expire(token, 0, TimeUnit.MILLISECONDS);
			redisTemplate.expire(personId, 0, TimeUnit.MILLISECONDS);
		}
		logger.info("personId {} token {} 移除token", personId, token);
	}
	
	@Override
	public String getToken(String personId) {
		if (personId == null) {
			return null;
		}
		return (String) redisTemplate.opsForValue().get(personId);
	}
	
	@Override
	public boolean verifyToken(HttpServletRequest request) {
		String token = getToken(request);
		return verifyToken(token);
	}
	
	@Override
	public String getToken(HttpServletRequest request) {
		if (request == null) {
			logger.info("request 为空");
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Constants.SESSION_ID)) {
					return cookie.getValue();
				}
			}
		}
		logger.info("获取 token为空");
		return null;
	}
	
	@Override
	public String getToken() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (servletRequestAttributes != null) {
			HttpServletRequest request = servletRequestAttributes.getRequest();
			return getToken(request);
		}
		logger.info("request 为空");
		return null;
	}
	
	@Override
	public TokenAuth getTokenAuth(String token) {
		if (StringUtils.isEmpty(token)) {
			logger.info("token 为空");
			return null;
		}
		TokenAuth tokenAuth = (TokenAuth) redisTemplate.opsForValue().get(token);
		if (tokenAuth == null || tokenAuth.getPersonId() == null) {
			logger.info("tokenAuth 为空 ");
			return null;
		}
		return tokenAuth;
	}
	
	@Override
	public TokenAuth getTokenAuthByPersonId(String personId) {
		String token = getToken(personId);
		if (token == null) {
			return null;
		}
		return getTokenAuth(token);
	}
	
	@Override
	public TokenAuth getTokenAuth(HttpServletRequest request) {
		String token = getToken(request);
		return getTokenAuth(token);
	}
	
	private HttpServletRequest getRequest() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (servletRequestAttributes != null) {
			return servletRequestAttributes.getRequest();
		}
		logger.info("request 为空");
		return null;
	}
	
	private HttpServletResponse getResponse() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (servletRequestAttributes != null) {
			return servletRequestAttributes.getResponse();
		}
		logger.info("request 为空");
		return null;
	}
	
	@Override
	public TokenAuth getTokenAuth() {
		return getTokenAuth(getRequest());
	}
	
	@Override
	public void updateEquipmentIdByToken(String token) {
		TokenAuth tokenAuth = getTokenAuth(token);
		if (tokenAuth == null) {
			return;
		}
		if (StringUtils.isEmpty(tokenAuth.getPersonId())) {
			return;
		}
		tokenAuth.setIp(Constants.LOCALHOST);
		tokenAuth.setEquipmentId(Constants.EQUIPMENT_ID);
		redisTemplate.opsForValue().set(token, tokenAuth, imConfig.getSessionExpireTime(), TimeUnit.SECONDS);
		redisTemplate.opsForValue().set(tokenAuth.getPersonId(), token, imConfig.getSessionExpireTime(), TimeUnit.SECONDS);
	}
	
	@Override
	public String getPersonId(String token) {
		TokenAuth tokenAuth = getTokenAuth(token);
		if (tokenAuth != null) {
			return tokenAuth.getPersonId();
		}
		logger.info("tokenAuth 为空");
		return null;
	}
	
	@Override
	public String getPersonId(HttpServletRequest request) {
		TokenAuth tokenAuth = getTokenAuth(request);
		if (tokenAuth != null) {
			return tokenAuth.getPersonId();
		}
		logger.info("tokenAuth 为空");
		return null;
	}
	
	@Override
	public String getPersonId() {
		TokenAuth tokenAuth = getTokenAuth();
		if (tokenAuth == null) {
			logger.info("tokenAuth 为空");
			return null;
		}
		return tokenAuth.getPersonId();
	}
}
