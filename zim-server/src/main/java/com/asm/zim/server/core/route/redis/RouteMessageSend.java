package com.asm.zim.server.core.route.redis;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.core.service.DataProtocolService;
import com.asm.zim.server.core.service.SendMessageService;
import com.asm.zim.server.service.TokenService;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

/**
 * @author : azhao
 * @description 发送消息 channel 路由 通知
 */
@Component
public class RouteMessageSend {
	private Logger logger = LoggerFactory.getLogger(RouteMessageSend.class);
	
	@Autowired
	private DataProtocolService dataProtocolService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private SendMessageService sendMessageService;
	
	/**
	 * 通过personId 路由转发消息
	 *
	 * @param json
	 */
	public void sendMessage(String json) {
		Gson gson = new Gson();
		NetMessage netMessage = gson.fromJson(json, NetMessage.class);
		BaseMessage.Message message = dataProtocolService.coverNetMessageToProtoMessage(netMessage);
		String toId = message.getToId();
		TokenAuth tokenAuth = tokenService.getTokenAuthByPersonId(toId);
		if (tokenAuth != null) {
			Channel channel = sendMessageService.sendByToken(message);
			if (channel == null) {
				logger.info(" IP:{} 路由发送消息", tokenAuth.getIp());
			}
		}
	}
	
	/**
	 * 通过 token 路由转发消息
	 *
	 * @param json
	 */
	public void sendMessageByToken(String json) {
		Gson gson = new Gson();
		NetMessage netMessage = gson.fromJson(json, NetMessage.class);
		BaseMessage.Message message = dataProtocolService.coverNetMessageToProtoMessage(netMessage);
		if (StringUtils.isEmpty(message.getToken())) {
			Channel channel = sendMessageService.sendByToken(message);
			if (channel == null) {
				logger.info(" token {} 路由发送消息", message.getToken());
			}
		}
	}
	
}
