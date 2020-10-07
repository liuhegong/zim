package com.asm.zim.server.core.chat;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.constants.MessageCategory;
import com.asm.zim.common.constants.MessageReadState;
import com.asm.zim.common.constants.MessageType;
import com.asm.zim.common.entry.FileResponse;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.file.client.api.FileManageService;
import com.asm.zim.server.core.container.LocalChannelGroup;
import com.asm.zim.server.core.service.DataProtocolService;
import com.asm.zim.server.core.service.SendMessageService;
import com.asm.zim.server.dao.MessageFileDao;
import com.asm.zim.server.entry.Message;
import com.asm.zim.server.entry.MessageFile;
import com.asm.zim.server.service.MessageService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : azhao
 * @description
 */
@Service("privateChatService")
public class PrivateChatService extends ChatService {
	private Logger logger = LoggerFactory.getLogger(PrivateChatService.class);
	@Autowired
	private FileManageService fileManageService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private LocalChannelGroup socketChannelGroup;
	@Autowired
	private MessageFileDao messageFileDao;
	@Autowired
	private DataProtocolService dataProtocolService;
	@Autowired
	private SystemChatService systemChatService;
	@Autowired
	private SendMessageService sendMessageService;
	
	/**
	 * 保存消息并返回相应
	 *
	 * @param msg
	 */
	private void saveSendAndResponse(BaseMessage.Message msg) {
		BaseMessage.Message.Builder sendBuilder = msg.toBuilder();
		systemChatService.systemConfirmMessage(sendBuilder.build());
		sendBuilder.setMessageType(MessageType.Ordinary);
		//发送的是文件
		Message message = dataProtocolService.coverProtoMessageToEntry(msg);
		if (message.getMessageCategory() == MessageCategory.File) {
			messageFileDao.insert(message.getMessageFile());
		}
		logger.info("开始保存消息");
		saveSendMessage(message);
	}
	
	/**
	 * 保存消息并接收消息
	 *
	 * @param msg
	 */
	private void saveReceiveAndReceive(BaseMessage.Message msg) {
		BaseMessage.Message.Builder receiveBuilder = msg.toBuilder();
		String messageId = IdUtil.fastSimpleUUID();
		receiveBuilder.setId(IdUtil.fastSimpleUUID());
		receiveBuilder.setMessageType(MessageType.Ordinary);
		Message receiveMessage = dataProtocolService.coverProtoMessageToEntry(msg);
		receiveMessage.setPersonId(msg.getToId());
		receiveMessage.setId(messageId);
		//接收的是文件
		if (msg.getMessageCategory() == MessageCategory.File) {
			//接收文件复制 不共用
			String token = msg.getToken();
			MessageFile messageFile = receiveMessage.getMessageFile();
			FileResponse fileResponse = fileManageService.copy(messageFile.getId(), token);
			if (fileResponse != null) {
				messageFile.setId(fileResponse.getId());
				messageFile.setUrl(fileResponse.getUrl());
				messageFile.setMessageId(messageId);
				messageFileDao.insert(messageFile);
			}
		}
		String toId = msg.getToId();
		Channel channel = sendMessageService.sendByToId(msg);
		if (channel != null) {
			logger.info("toId {} 在线发送", toId);
		} else {
			logger.info("toId {} 的离线消息", toId);
		}
		logger.info("开始保存消息");
		saveReceiveMessage(receiveMessage);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleChatMessage(BaseMessage.Message msg) {
		logger.info("私聊");
		saveSendAndResponse(msg);
		saveReceiveAndReceive(msg);
	}
	
	/**
	 * 保存发送消息到数据库
	 */
	private void saveSendMessage(Message sendMessage) {
		sendMessage.setPersonId(sendMessage.getFromId());
		sendMessage.setReadState(MessageReadState.HAS_READ);
		messageService.addMessage(sendMessage);
	}
	
	/**
	 * 保存接收消息
	 */
	private void saveReceiveMessage(Message receiveMessage) {
		receiveMessage.setReadState(MessageReadState.UN_READ);
		messageService.addMessage(receiveMessage);
	}
}
