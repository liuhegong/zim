package com.asm.zim.server.core.handler;

import com.asm.zim.common.constants.MessageCode;
import com.asm.zim.common.constants.MessageType;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.core.chat.SystemChatService;
import com.asm.zim.server.core.service.ChangeMessageService;
import com.asm.zim.server.service.TokenService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description
 */
@Component("loginHandler")
@ChannelHandler.Sharable
public class LoginHandler extends SimpleChannelInboundHandler<BaseMessage.Message> {
	private Logger logger = LoggerFactory.getLogger(LoginHandler.class);
	@Resource(name = "changeMessageService")
	private ChangeMessageService changeMessageService;
	@Autowired
	private SystemChatService systemChatService;
	@Autowired
	private TokenService tokenService;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseMessage.Message message) throws Exception {
		int messageType = message.getMessageType();
		int code = message.getCode();
		if (messageType == MessageType.System && code == MessageCode.LoginRequest) {
			logger.info("登录请求{}", message.toString());
			boolean hasLogin = systemChatService.login(ctx, message);
			logger.info("登录是否成功 {}",hasLogin);
		}
		String token = message.getToken();
		if (StringUtils.isEmpty(token)) {
			logger.info("token 为空");
			ctx.channel().close();
			return;
		} else {
			String personId = tokenService.getPersonId(token);
			if (personId == null) {
				ctx.channel().close();
				logger.debug("未登录");
				ctx.channel().close();
				return;
			}
		}
		ctx.fireChannelRead(message);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelActive");
		changeMessageService.channelActive(ctx);
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelInactive");
		changeMessageService.channelInactive(ctx);
		super.channelInactive(ctx);
	}
}
