package com.asm.zim.common.constants;

/**
 * @author : azhao
 * @description 返回码
 */
public enum ResultCode {
	Success(200, "成功"),
	Fail(-1, "失败"),
	ES_Error(1, "es错误"),
	Argument_Error(1, "400 参数解析异常"),
	Request_Method_Error(2, "请求方法找不到"),
	Missing_Argument_Error(3, "缺失参数"),
	Service_Error(-2, "服务器未知错误"),
	Redis_Time_Out(-3, "缓存连接超时"),
	Redis_Connect_Failure(-3, "缓存连接失败"),
	UnLogin(10001, "未登录");
	private int code;
	private String message;
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	ResultCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ResultCode{" +
				"code=" + code +
				", message='" + message + '\'' +
				'}';
	}
}

