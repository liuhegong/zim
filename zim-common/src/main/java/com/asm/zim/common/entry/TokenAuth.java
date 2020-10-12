package com.asm.zim.common.entry;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Data
public class TokenAuth implements Serializable, Cloneable {
	private String personId;
	private String ip;
	private String token;
	private String equipmentId;
	/**
	 * 上一次登录时间
	 */
	private long lastLoginTime;
	
	public TokenAuth() {
	}
	
}
