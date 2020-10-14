
package com.asm.zim.server.core.route.redis;


import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.config.route.RedisRouteCondition;
import com.asm.zim.server.core.route.RouteMessage;
import com.asm.zim.server.core.service.DataProtocolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Component
@Conditional(RedisRouteCondition.class)
public class RedisRouteMessage implements RouteMessage {
	private Logger logger = LoggerFactory.getLogger(RedisRouteMessage.class);
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	@Autowired
	private DataProtocolService dataProtocolService;
	
	@PostConstruct
	public void init() {
		logger.info("加载Redis 路由方式");
	}
	
	@Override
	public void sendMessageByPersonId(NetMessage netMessage) {
		redisTemplate.convertAndSend(Constants.REDIS_MESSAGE_SEND_TOPIC, netMessage);
	}
	
	@Override
	public void sendMessageByPersonId(BaseMessage.Message message) {
		NetMessage netMessage = dataProtocolService.coverProtoMessageToNetMessage(message);
		redisTemplate.convertAndSend(Constants.REDIS_MESSAGE_SEND_TOPIC, netMessage);
	}
}
