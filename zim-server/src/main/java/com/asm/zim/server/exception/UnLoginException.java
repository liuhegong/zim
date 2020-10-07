package com.asm.zim.server.exception;

/**
 * @author : azhao
 * @description
 */
public class UnLoginException extends BaseException {
	public UnLoginException() {
	}
	
	public UnLoginException(int code, String message) {
		super(code, message);
	}
}
