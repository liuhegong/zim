package com.asm.zim.common.entry;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description protobuf 对应的实体类
 */
@Data
public class NetMessage implements Serializable, Cloneable {
	private String id;
	private int code;//系统消息生效 系统功能码
	private String fromId;
	private String toId;
	private String content;
	private long sendTime;
	private int messageType; //1：系统消息  2：普通消息
	private int messageCategory;// 消息类别
	private int chatType; //聊天类型 1.群聊 2.私聊 3.消息推送
	private String terminalType; //终端类型
	private String protocol;//协议
	private String token;
	private String data;//其他属性
	private NetMessageFile netMessageFile;
}
