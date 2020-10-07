package com.asm.zim.server.entry;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description 日志记录
 */
@Data
@TableName("log")
public class Log implements Serializable {
	@TableId
	private String id;
	@TableField("person_id")
	private String personId;
	@TableField("server_ip")
	private String serverIp;
	@TableField("http_port")
	private int httpPort;
	@TableField("websocket_port")
	private int webSocketPort;
	@TableField("record_time")
	private long recordTime;
	@TableField("level")
	private int level;
	@TableField("module")
	private String module;
	@TableField("describe")
	private String describe;
	/**
	 * 请求参数
	 */
	@TableField("arg")
	private String arg;
	/**
	 * 返回结果
	 */
	@TableField("result")
	private String result;
	@TableField("method")
	private String method;
}
