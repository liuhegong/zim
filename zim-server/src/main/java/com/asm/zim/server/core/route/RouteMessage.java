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
	void sendMessageByPersonId(NetMessage netMessage);
	
	void sendMessageByPersonId(BaseMessage.Message message);
	
}
