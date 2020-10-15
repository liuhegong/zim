package com.asm.zim.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * @author : azhao
 * @description
 */
public class RabbitConfig {
	private Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
	
	public static final String MESSAGE_QUEUE = "chat_message_queue";
	
	@Bean
	public Queue messageQueue() {
		return new Queue(MESSAGE_QUEUE, true);
	}
	
}
