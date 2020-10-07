package com.asm.zim.common.constants;

import java.io.Serializable;

/**
 * @author azhao
 */
public class Result<T> implements Serializable {
	private static final long serialVersionUID = -6630479982832317400L;
	/**
	 * 错误码
	 */
	private Integer code = ResultCode.Success.getCode();
	/**
	 * 失败消息
	 */
	private String message;
	/**
	 * 数据
	 */
	private T data;
	
	public Result<T> failure() {
		this.code = ResultCode.Fail.getCode();
		this.message = ResultCode.Fail.getMessage();
		return this;
	}
	
	public Result<T> failure(String message) {
		this.code = ResultCode.Fail.getCode();
		this.message = message;
		return this;
	}
	
	public Result<T> failure(int code, String message) {
		this.code = code;
		this.message = message;
		return this;
	}
	
	public Result<T> failure(ResultCode resultCode) {
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
		return this;
	}
	
	public Result<T> failure(ResultCode resultCode, T data) {
		this.data = data;
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
		return this;
	}
	
	public Result<T> success(T data) {
		this.code = ResultCode.Success.getCode();
		this.message = ResultCode.Success.getMessage();
		this.data = data;
		return this;
	}
	
	public Result<T> success() {
		this.code = ResultCode.Success.getCode();
		this.message = ResultCode.Success.getMessage();
		return this;
	}
	
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public Result(ResultCode resultCode) {
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}
	
	public Result(Integer code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public Result(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public Result() {
	}
	
	@Override
	public String toString() {
		return "Result{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
