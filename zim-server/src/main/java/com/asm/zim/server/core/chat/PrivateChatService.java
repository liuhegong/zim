package com.asm.zim.server.core.chat;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.constants.MessageType;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.config.RabbitConfig;
import com.asm.zim.server.core.service.DataProtocolService;
import com.asm.zim.server.core.service.SendMessageService;
import com.asm.zim.server.dao.MessageFileDao;
import com.asm.zim.server.entry.Message;
import com.asm.zim.server.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
	private MessageService messageService;
	@Autowired
	private MessageFileDao messageFileDao;
	@Autowired
	private DataProtocolService dataProtocolService;
	@Autowired
	private SystemChatService systemChatService;
	@Autowired
	private SendMessageService sendMessageService;
	@Autowired(required = false)
	private RabbitTemplate rabbitTemplate;
	
	/**
	 * 保存消息并返回相应
	 *
	 * @param msg
	 */
	private void saveSendAndResponse(BaseMessage.Message msg) {
		BaseMessage.Message.Builder sendBuilder = msg.toBuilder();
		String id = IdUtil.fastSimpleUUID();
		sendBuilder.setContent(id);//将真实的id返回给客户端
		systemChatService.systemConfirmMessage(sendBuilder.build());
		sendBuilder.setMessageType(MessageType.Ordinary);
		//发送的是文件
		Message message = dataProtocolService.coverProtoMessageToEntry(msg);
		message.setId(id);
		if (rabbitTemplate != null) {
			logger.info("mq发送消息保存");
			rabbitTemplate.convertAndSend(RabbitConfig.SEND_MESSAGE_QUEUE, message);
		} else {
			messageService.saveSend(message);
		}
	}
	
	/**
	 * 保存消息并接收消息
	 *
	 * @param msg
	 */
	private void saveReceiveAndReceive(BaseMessage.Message msg) {
		BaseMessage.Message.Builder receiveBuilder = msg.toBuilder();
		receiveBuilder.setId(IdUtil.fastSimpleUUID());
		receiveBuilder.setMessageType(MessageType.Ordinary);
		BaseMessage.Message receiveBaseMessage = receiveBuilder.build();
		sendMessageService.sendByToId(receiveBaseMessage);
		Message message = dataProtocolService.coverProtoMessageToEntry(receiveBaseMessage);
		message.setPersonId(msg.getToId());
		if (rabbitTemplate != null) {
			logger.info("mq接收消息保存");
			rabbitTemplate.convertAndSend(RabbitConfig.RECEIVE_MESSAGE_QUEUE, message);
		} else {
			messageService.saveReceive(message);
		}
		
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleChatMessage(BaseMessage.Message msg) {
		logger.info("私聊");
		saveSendAndResponse(msg);
		saveReceiveAndReceive(msg);
	}
}
