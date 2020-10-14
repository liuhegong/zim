package com.asm.zim.server.core.service;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.core.container.LocalChannelGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : azhao
 * @description
 */
@Service
public class SendMessageService {
	private Logger logger = LoggerFactory.getLogger(SendMessageService.class);
	@Autowired
	private LocalChannelGroup localChannelGroup;
	@Autowired
	private DataProtocolService dataProtocolService;
	
	public Channel sendByToId(BaseMessage.Message msg) {
		Channel channel = localChannelGroup.getChannelByPerson(msg.getToId());
		return send(channel, msg);
	}
	
	public Channel sendByToId(NetMessage netMessage) {
		return sendByToId(dataProtocolService.coverNetMessageToProtoMessage(netMessage));
	}
	
	/**
	 * 发送给客户端的数据
	 *
	 * @param channel
	 * @param msg
	 * @return
	 */
	private Channel send(Channel channel, BaseMessage.Message msg) {
		if (channel != null) {
			ChannelPipeline pipeline = channel.pipeline();
			//todo 通过编码器处理
			if (pipeline.get("webSocketHandler") != null) {
				logger.info("webSocket");
				channel.writeAndFlush(dataProtocolService.msgToBinWebSocket(msg));
			} else {
				logger.info("tcpSocket");
				channel.writeAndFlush(msg);
			}
		}
		return channel;
	}
	
	public Channel sendByToken(BaseMessage.Message msg) {
		Channel channel = localChannelGroup.getChannelByToken(msg.getToken());
		return send(channel, msg);
	}
}
