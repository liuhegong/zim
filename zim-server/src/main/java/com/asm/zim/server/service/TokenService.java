package com.asm.zim.server.service;

import com.asm.zim.common.entry.TokenAuth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : azhao
 * @description
 */
public interface TokenService {
	void setCookie(String token);
	
	/**
	 * 刷新token
	 *
	 * @param token
	 */
	void refreshByToken(String token);
	
	
	void setCookie(String token, HttpServletResponse response);
	
	/**
	 * 创建token 包括 tokenAuth
	 *
	 * @param personId
	 * @return 返回token
	 */
	String createToken(String personId);
	
	/**
	 * 返回personId
	 *
	 * @param token
	 * @return
	 */
	boolean verifyToken(String token);
	
	
	void removeToken(String token);
	
	/**
	 * 通过personId 获取token
	 *
	 * @param personId
	 * @return
	 */
	String getToken(String personId);
	
	/**
	 * 通过request 获取token
	 *
	 * @param request
	 * @return
	 */
	String getToken(HttpServletRequest request);
	
	/**
	 * 通过 request 获取 token
	 *
	 * @return
	 */
	String getToken();
	
	/**
	 * 通过请求验证token
	 *
	 * @param request
	 * @return
	 */
	boolean verifyToken(HttpServletRequest request);
	
	TokenAuth getTokenAuth(String token);
	
	TokenAuth getTokenAuthByPersonId(String personId);
	
	/**
	 * 通过 request 获取TokenAuth
	 *
	 * @param request
	 * @return
	 */
	TokenAuth getTokenAuth(HttpServletRequest request);
	
	/**
	 * 通过 request 获取TokenAuth
	 *
	 * @return
	 */
	TokenAuth getTokenAuth();
	
	/**
	 * 通过token 修改 设备唯一标识
	 */
	void updateEquipmentIdByToken(String token);
	
	/**
	 * 通过 token  获取 personId
	 *
	 * @param token
	 * @return
	 */
	String getPersonId(String token);
	
	/**
	 * 通过 request  获取 personId
	 *
	 * @param request
	 * @return
	 */
	String getPersonId(HttpServletRequest request);
	
	/**
	 * 通过request 获取 personId
	 *
	 * @return
	 */
	String getPersonId();
}
