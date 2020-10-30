package com.asm.zim.server.core.chat;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.constants.MessageReadState;
import com.asm.zim.common.constants.MessageType;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.config.queue.RabbitConfig;
import com.asm.zim.server.core.service.DataProtocolService;
import com.asm.zim.server.core.service.SendMessageService;
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
		//将真实的id返回给客户端
		sendBuilder.setContent(id);
		systemChatService.systemConfirmMessage(sendBuilder.build());
		Message message = dataProtocolService.coverProtoMessageToEntry(msg);
		message.setId(id);
		message.setContent(msg.getContent());
		message.setPersonId(message.getFromId());
		message.setReadState(MessageReadState.HAS_READ);
		if (rabbitTemplate != null) {
			logger.info("mq发送消息保存");
			rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_EXCHANGE,RabbitConfig.SEND_MESSAGE_QUEUE, message);
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
		//发给对方
		BaseMessage.Message.Builder receiveBuilder = msg.toBuilder();
		String id = IdUtil.fastSimpleUUID();
		receiveBuilder.setId(id);
		receiveBuilder.setMessageType(MessageType.Ordinary);
		BaseMessage.Message receiveBaseMessage = receiveBuilder.build();
		sendMessageService.sendByToId(receiveBaseMessage);
		//保存消息
		Message message = dataProtocolService.coverProtoMessageToEntry(msg);
		message.setPersonId(msg.getToId());
		message.setMessageType(MessageType.Ordinary);
		message.setReadState(MessageReadState.UN_READ);
		if (rabbitTemplate != null) {
			logger.info("mq接收消息保存");
			rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_EXCHANGE,RabbitConfig.RECEIVE_MESSAGE_QUEUE, message);
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
