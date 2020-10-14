package com.asm.zim.server.core.route.redis;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.core.service.ChangeMessageService;
import com.asm.zim.server.core.service.DataProtocolService;
import com.asm.zim.server.service.TokenService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	private ChangeMessageService changeMessageService;
	
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
		if (tokenAuth == null) {
			logger.info("toId {} 不在线", toId);
			return;
		}
		if (Constants.EQUIPMENT_ID.equals(tokenAuth.getEquipmentId())) {
			//本机处理
			logger.info("IP:{}消费了一条消息", tokenAuth.getIp());
			changeMessageService.handleRead(message);
		} else {
			logger.info("IP:{}忽略了一条消息", tokenAuth.getIp());
		}
	}
}
