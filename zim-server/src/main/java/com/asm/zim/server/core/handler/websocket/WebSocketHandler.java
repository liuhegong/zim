package com.asm.zim.server.core.handler.websocket;

import com.asm.zim.common.proto.BaseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : azhao
 * @description
 */
@Component("webSocketHandler")
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<BaseMessage.Message> {
	private Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseMessage.Message message) throws Exception {
		logger.info("webSocketHandler");
		ctx.fireChannelRead(message);
	}
}
