package com.asm.zim.server.core.service;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.entry.NetMessageFile;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.entry.Message;
import com.asm.zim.server.entry.MessageFile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author : azhao
 * @description 网络消息与实体消息转化
 */
@Service
public class DataProtocolService {
	private Logger logger = LoggerFactory.getLogger(DataProtocolService.class);
	/**
	 * protobuf Msg 封装 BinaryWebSocketFrame
	 *
	 * @param msg
	 * @return
	 */
	public BinaryWebSocketFrame msgToBinWebSocket(BaseMessage.Message msg) {
		ByteBuf byteBuf = Unpooled.wrappedBuffer(msg.toByteArray());
		return new BinaryWebSocketFrame(byteBuf);
	}
	
	public NetMessage coverProtoMessageToNetMessage(BaseMessage.Message msg) {
		NetMessage netMessage = new NetMessage();
		netMessage.setId(msg.getId());
		netMessage.setCode(msg.getCode());
		netMessage.setFromId(msg.getFromId());
		netMessage.setToId(msg.getToId());
		netMessage.setContent(msg.getContent());
		netMessage.setSendTime(msg.getSendTime());
		netMessage.setMessageType(msg.getMessageType());
		netMessage.setMessageCategory(msg.getMessageCategory());
		netMessage.setChatType(msg.getChatType());
		netMessage.setTerminalType(msg.getTerminalType());
		netMessage.setProtocol(msg.getProtocol());
		netMessage.setToken(msg.getToken());
		netMessage.setData(msg.getData());
		BaseMessage.MessageFile msgFile = msg.getMessageFile();
		if (msgFile!=null){
			NetMessageFile netMessageFile = new NetMessageFile();
			netMessageFile.setId(msgFile.getId());
			netMessageFile.setSuffix(msgFile.getSuffix());
			netMessageFile.setMessageId(netMessage.getId());
			netMessageFile.setUrl(msgFile.getUrl());
			netMessageFile.setCreateTime(msgFile.getCreateTime());
			netMessageFile.setFileName(msgFile.getFileName());
			netMessageFile.setSize(msgFile.getSize());
			netMessage.setNetMessageFile(netMessageFile);
		}
		return netMessage;
	}
	
	public BaseMessage.Message coverNetMessageToProtoMessage(NetMessage netMessage) {
		BaseMessage.Message.Builder builder = BaseMessage.Message.newBuilder();
		builder.setId(netMessage.getId());
		builder.setCode(netMessage.getCode());
		builder.setFromId(netMessage.getFromId());
		builder.setToId(netMessage.getToId());
		builder.setContent(netMessage.getContent());
		builder.setSendTime(netMessage.getSendTime());
		builder.setMessageType(netMessage.getMessageType());
		builder.setMessageCategory(netMessage.getMessageCategory());
		builder.setChatType(netMessage.getChatType());
		builder.setTerminalType(netMessage.getTerminalType());
		builder.setProtocol(netMessage.getProtocol());
		builder.setToken(netMessage.getToken());
		builder.setData(netMessage.getData());
		if (netMessage.getNetMessageFile() != null) {
			NetMessageFile netMessageFile = netMessage.getNetMessageFile();
			BaseMessage.MessageFile.Builder messageFileBuilder = BaseMessage.MessageFile.newBuilder();
			messageFileBuilder.setId(netMessageFile.getId());
			messageFileBuilder.setMessageId(netMessage.getId());
			messageFileBuilder.setFileName(netMessageFile.getFileName());
			messageFileBuilder.setSize(netMessageFile.getSize());
			messageFileBuilder.setSuffix(netMessageFile.getSuffix());
			messageFileBuilder.setUrl(netMessageFile.getUrl());
			messageFileBuilder.setCreateTime(netMessageFile.getCreateTime());
			builder.setMessageFile(messageFileBuilder.build());
		}
		return builder.build();
	}
	
	public Message coverProtoMessageToEntry(BaseMessage.Message msg) {
		Message message = new Message();
		message.setId(msg.getId());
		message.setFromId(msg.getFromId());
		message.setToId(msg.getToId());
		message.setContent(msg.getContent());
		message.setSendTime(msg.getSendTime());
		message.setChatType(msg.getChatType());
		message.setMessageType(msg.getMessageType());
		message.setMessageCategory(msg.getMessageCategory());
		message.setTerminalType(msg.getTerminalType());
		message.setProtocol(msg.getProtocol());
		message.setOrderNumber(msg.getSendTime());
		if (msg.getMessageFile() != null) {
			BaseMessage.MessageFile msgFile = msg.getMessageFile();
			MessageFile messageFile = new MessageFile();
			messageFile.setId(msgFile.getId());
			messageFile.setSuffix(msgFile.getSuffix());
			messageFile.setMessageId(message.getId());
			messageFile.setUrl(msgFile.getUrl());
			messageFile.setCreateTime(msgFile.getCreateTime());
			messageFile.setFileName(msgFile.getFileName());
			messageFile.setSize(msgFile.getSize());
			message.setMessageFile(messageFile);
		}
		return message;
	}
	
}
