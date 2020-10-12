package com.asm.zim.server.core.route.redis;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.config.yaml.WebsocketConfig;
import com.asm.zim.server.core.service.ChangeMessageService;
import com.asm.zim.server.core.service.DataProtocolService;
import com.asm.zim.server.service.TokenService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description redis 接收消息路由通知
 */
@Component
public class RouteMessageReceiver {
	private Logger logger = LoggerFactory.getLogger(RouteMessageReceiver.class);
	@Resource(name = "changeMessageService")
	private ChangeMessageService changeMessageService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private WebsocketConfig websocketConfig;
	@Autowired
	private DataProtocolService dataProtocolService;
	
	/**
	 * 消息订阅
	 *
	 * @param json
	 */
	public void receiveMessage(String json) {
		Gson gson = new Gson();
		NetMessage netMessage = gson.fromJson(json, NetMessage.class);
		BaseMessage.Message message = dataProtocolService.coverNetMessageToProtoMessage(netMessage);
		TokenAuth tokenAuth = tokenService.getTokenAuthByPersonId(message.getToId());
		if (Constants.EQUIPMENT_ID.equals(tokenAuth.getEquipmentId())) {
			logger.info("当前机器 IP:{},port:{} 消费了一条消息", tokenAuth.getIp(), tokenAuth.getPort());
			changeMessageService.handleRead(message);
		} else {
			logger.info("IP:{},port:{} 忽略消息", tokenAuth.getIp(), tokenAuth.getPort());
		}
	}
}
