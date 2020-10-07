package com.asm.zim.server.core.chat;

import com.asm.zim.common.proto.BaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author : azhao
 * @description
 */
@Service("groupChatService")
public class GroupChatService extends ChatService {
	private Logger logger = LoggerFactory.getLogger(GroupChatService.class);
	
	@Override
	public void handleChatMessage( BaseMessage.Message msg) {
		logger.info("群聊");
	}
	
}
