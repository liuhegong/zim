package com.asm.zim.server.core.route.netty;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.config.route.NettyRouteCondition;
import com.asm.zim.server.core.route.RouteMessage;
import com.asm.zim.server.core.service.ChangeMessageService;
import com.asm.zim.server.core.service.DataProtocolService;
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
	
	@PostConstruct
	public void init() {
		logger.info("加载Netty 路由方式");
	}
	
	@Override
	public void sendMessageByPersonId(NetMessage netMessage) {
		sendMessageByPersonId(dataProtocolService.coverNetMessageToProtoMessage(netMessage));
	}
	
	private void sendMessage(TokenAuth tokenAuth, BaseMessage.Message message) {
		if (Constants.EQUIPMENT_ID.equals(tokenAuth.getEquipmentId())) {
			//本机处理
			logger.info("IP:{}本机处理消息", tokenAuth.getIp());
			changeMessageService.handleRead(message);
			return;
		}
		logger.info("非本机处理消息转发 {}",tokenAuth.getIp());
		Channel channel = routeClient.getChannel(tokenAuth.getIp());
		channel.writeAndFlush(message);
	}
	
	@Override
	public void sendMessageByPersonId(BaseMessage.Message message) {
		TokenAuth tokenAuth = tokenService.getTokenAuthByPersonId(message.getToId());
		if (tokenAuth == null) {
			logger.info("tokeAuth为空,toId 为 {}",message.getToId());
			return;
		}
		sendMessage(tokenAuth, message);
	}
	
}
