package com.asm.zim.server.core.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * key 过期监听
 *
 * @author : azhao
 * @description
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
	private Logger logger = LoggerFactory.getLogger(RedisKeyExpirationListener.class);
	
	@Autowired
	private LocalChannelGroup socketChannelGroup;
	
	public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		//String expiredKey = message.toString();
		//删除key
		//socketChannelGroup.removeChannel(expiredKey);
	}
}
