
package com.asm.zim.server.core.route.redis;


import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.core.route.RouteMessage;
import com.asm.zim.server.core.service.DataProtocolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

public class RedisRouteMessage implements RouteMessage {
	private Logger logger = LoggerFactory.getLogger(RedisRouteMessage.class);
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	@Autowired
	private DataProtocolService dataProtocolService;
	
	@Override
	public void sendMessage(NetMessage netMessage) {
		redisTemplate.convertAndSend(Constants.REDIS_MESSAGE_RECEIVE_TOPIC, netMessage);
	}
	
	@Override
	public void sendMessage(BaseMessage.Message message) {
		NetMessage netMessage = dataProtocolService.coverProtoMessageToNetMessage(message);
		sendMessage(netMessage);
	}
	
	@Override
	public void receiveMessage(NetMessage netMessage) {
		redisTemplate.convertAndSend(Constants.REDIS_MESSAGE_SEND_TOPIC, netMessage);
	}
	
	@Override
	public void receiveMessage(BaseMessage.Message message) {
		NetMessage netMessage = dataProtocolService.coverProtoMessageToNetMessage(message);
		receiveMessage(netMessage);
	}
}
