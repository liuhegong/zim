package com.asm.zim.server.exception;

import com.asm.zim.common.constants.Result;
import com.asm.zim.common.constants.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author : azhao
 * @description
 */
@Order(1)
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		logger.error("400 参数解析异常", e);
		e.printStackTrace();
//---
		return new Result<String>().failure(ResultCode.Argument_Error);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		e.printStackTrace();
		return new Result<String>().failure(ResultCode.Request_Method_Error);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Result<String> missingServletRequestParameterException(MissingServletRequestParameterException e) {
		e.printStackTrace();
		return new Result<String>().failure(ResultCode.Missing_Argument_Error);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UnLoginException.class)
	public Result<String> unLoginException(UnLoginException e) {
		logger.error("未登录", e);
		return new Result<String>().failure(ResultCode.UnLogin);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	public Result<String> handleException(Exception e) {
		logger.error("500 服务运行异常", e);
		e.printStackTrace();
		return new Result<String>().failure(ResultCode.Service_Error);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(DataAccessResourceFailureException.class)
	public Result<String> esError(DataAccessResourceFailureException e) {
		logger.error("esError 数据访问异常", e);
		e.printStackTrace();
		return new Result<String>().failure(ResultCode.ES_Error);
	}
}
