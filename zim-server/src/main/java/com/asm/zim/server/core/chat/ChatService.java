package com.asm.zim.server.core.chat;

import com.asm.zim.common.proto.BaseMessage;

/**
 * @author : azhao
 * @description
 */
public abstract class ChatService {
	/**
	 * 处理发过来的消息
	 *
	 * @param msg
	 */
	public abstract void handleChatMessage( BaseMessage.Message msg);
	
}
