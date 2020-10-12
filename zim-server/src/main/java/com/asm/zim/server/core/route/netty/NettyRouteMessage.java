package com.asm.zim.server.core.route.netty;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.config.route.NettyRouteCondition;
import com.asm.zim.server.core.route.RouteMessage;
import com.asm.zim.server.core.service.ChangeMessageService;
import com.asm.zim.server.core.service.DataProtocolService;
import com.asm.zim.server.core.service.SendMessageService;
import com.asm.zim.server.service.TokenService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : azhao
 * @description
 */
@Component
@Conditional(NettyRouteCondition.class)
public class NettyRouteMessage implements RouteMessage {
	private Logger logger = LoggerFactory.getLogger(NettyRouteMessage.class);
	@Autowired
	private TokenService tokenService;
	@Autowired
	private DataProtocolService dataProtocolService;
	@Autowired
	private RouteClient routeClient;
	@Autowired
	private ChangeMessageService changeMessageService;
	@Autowired
	private SendMessageService sendMessageService;
	
	@PostConstruct
	public void init() {
		logger.info("加载Netty 路由方式");
	}
	
	@Override
	public void sendMessage(NetMessage netMessage) {
		TokenAuth tokenAuth = tokenService.getTokenAuthByPersonId(netMessage.getToId());
		if (tokenAuth == null) {
			logger.debug("tokenAuth toId {} 为空", netMessage.getToId());
			return;
		}
		BaseMessage.Message message = dataProtocolService.coverNetMessageToProtoMessage(netMessage);
		if (Constants.EQUIPMENT_ID.equals(tokenAuth.getEquipmentId())) {
			//本机处理
			logger.info("IP:{}消费了一条消息", tokenAuth.getIp());
			changeMessageService.handleRead(message);
			return;
		}
		logger.info("非本机处理");
		//非本机处理消息转发
		Channel channel = routeClient.getChannel(tokenAuth.getIp());
		channel.writeAndFlush(message);
	}
	
	@Override
	public void sendMessage(BaseMessage.Message message) {
		sendMessage(dataProtocolService.coverProtoMessageToNetMessage(message));
	}
	
	@Override
	public void receiveMessage(NetMessage netMessage) {
		BaseMessage.Message message = dataProtocolService.coverNetMessageToProtoMessage(netMessage);
		Channel channel = sendMessageService.sendByToken(message);
		if (channel != null) {
			logger.info("消息发送给 token为 {}", netMessage.getToken());
			return;
		}
		logger.info("channel 为空");
	}
	
	@Override
	public void receiveMessage(BaseMessage.Message message) {
		Channel channel = sendMessageService.sendByToken(message);
		if (channel != null) {
			logger.info("消息发送给 token为 {}", message.getToken());
			return;
		}
		logger.info("channel 为空");
	}
}
