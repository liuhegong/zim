package com.asm.zim.file.server.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Getter
@Setter
public class BaseException extends Exception implements Serializable {
	private int code;
	private String message;
	
	public BaseException() {
	
	}
	
	public BaseException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
}
