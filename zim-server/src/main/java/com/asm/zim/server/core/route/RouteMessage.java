package com.asm.zim.server.core.route;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.proto.BaseMessage;

/**
 * @author : azhao
 * @description 路由转发消息
 */
public interface RouteMessage {
	/**
	 * 路由发送消息到各个系统
	 *
	 * @param netMessage
	 */
	void sendMessage(NetMessage netMessage);
	
	void sendMessage(BaseMessage.Message message);
	
	/**
	 * 各个系统收到消息
	 *
	 * @param netMessage
	 */
	void receiveMessage(NetMessage netMessage);
	
	void receiveMessage(BaseMessage.Message message);
}
