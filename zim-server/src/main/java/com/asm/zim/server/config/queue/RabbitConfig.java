package com.asm.zim.server.config.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : azhao
 * @description
 */
@Configuration
public class RabbitConfig {
	private Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
	
	public static final String SEND_MESSAGE_QUEUE = "send.message.queue";
	
	public static final String RECEIVE_MESSAGE_QUEUE = "receive.message.queue";
	
	public static final String MESSAGE_EXCHANGE = "message_exchange";
	
	@Bean(MESSAGE_EXCHANGE)
	public DirectExchange messageDirectExchange() {
		return new DirectExchange(MESSAGE_EXCHANGE);
	}
	
	/**
	 * 持久化 发送消息队列
	 *
	 * @return
	 */
	@Bean(SEND_MESSAGE_QUEUE)
	public Queue sendMessageQueue() {
		return new Queue(SEND_MESSAGE_QUEUE, true);
	}
	
	/**
	 * 持久化 接收消息队列
	 *
	 * @return
	 */
	@Bean(RECEIVE_MESSAGE_QUEUE)
	public Queue receiveMessageQueue() {
		return new Queue(RECEIVE_MESSAGE_QUEUE, true);
	}
	
	@Bean
	public Binding bindingSendMessage(@Qualifier(SEND_MESSAGE_QUEUE) Queue sendMessageQueue,
	                                  @Qualifier(MESSAGE_EXCHANGE) DirectExchange messageDirectExchange) {
		return BindingBuilder.bind(sendMessageQueue).to(messageDirectExchange).with(SEND_MESSAGE_QUEUE);
	}
	
	@Bean
	public Binding bindingReceiveMessage(@Qualifier(RECEIVE_MESSAGE_QUEUE) Queue receiveMessageQueue,
	                                     DirectExchange messageDirectExchange) {
		return BindingBuilder.bind(receiveMessageQueue).to(messageDirectExchange).with(RECEIVE_MESSAGE_QUEUE);
	}
	
	/*@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		*//*rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
		});*//*
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
			logger.info("消息从Exchange路由到Queue失败: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}", exchange, routingKey, replyCode, replyText, message);
		});
		return rabbitTemplate;
	}*/
	
}
