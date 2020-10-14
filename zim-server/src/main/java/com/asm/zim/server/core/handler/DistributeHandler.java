package com.asm.zim.server.core.handler;

import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.core.route.RouteMessage;
import com.asm.zim.server.core.service.ChangeMessageService;
import com.asm.zim.server.service.TokenService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description 路由处理分发的消息
 */
@Component("distributeHandler")
@ChannelHandler.Sharable
public class DistributeHandler extends SimpleChannelInboundHandler<BaseMessage.Message> {
	private Logger logger = LoggerFactory.getLogger(DistributeHandler.class);
	@Autowired
	private TokenService tokenService;
	
	@Resource(name = "changeMessageService")
	private ChangeMessageService changeMessageService;
	@Autowired
	private RouteMessage routeMessage;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseMessage.Message message) throws Exception {
		String toId = message.getToId();
		TokenAuth tokenAuth = tokenService.getTokenAuthByPersonId(toId);
		if (tokenAuth == null) {
			logger.info("toId {} 不在线本机处理", toId);
			changeMessageService.handleRead(ctx,message);
			return;
		}
		logger.info("toId 为 {} 在线消息路由转发", toId);
		routeMessage.sendMessageByPersonId(message);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		changeMessageService.channelInactive(ctx);
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			logger.info("websocket 握手成功。");
			/*WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
			String requestUri = handshakeComplete.requestUri();
			logger.info("requestUri:[{}]", requestUri);
			String subProtocol = handshakeComplete.selectedSubprotocol();
			logger.info("subProtocol:[{}]", subProtocol);
			handshakeComplete.requestHeaders().forEach(entry -> logger.info("header key:[{}] value:[{}]", entry.getKey(), entry.getValue()));*/
		} else {
			super.userEventTriggered(ctx, evt);
		}
		super.userEventTriggered(ctx, evt);
	}
	
}
