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
	
	public static final String SEND_MESSAGE_QUEUE = "send_message_queue";
	
	public static final String RECEIVE_MESSAGE_QUEUE = "receive_message_queue";
	
	/**
	 * 持久化 发送消息队列
	 *
	 * @return
	 */
	@Bean
	public Queue sendMessageQueue() {
		return new Queue(SEND_MESSAGE_QUEUE, true);
	}
	
	/**
	 * 持久化 接收消息队列
	 *
	 * @return
	 */
	@Bean
	public Queue receiveMessageQueue() {
		return new Queue(RECEIVE_MESSAGE_QUEUE, true);
	}
}
