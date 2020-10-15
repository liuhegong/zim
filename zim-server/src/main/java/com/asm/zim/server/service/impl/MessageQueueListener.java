package com.asm.zim.server.service.impl;

import com.asm.zim.server.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.io.IOException;

/**
 * @author : azhao
 * @description
 */
@Component
public class MessageQueueListener {
	private Logger logger = LoggerFactory.getLogger(MessageQueueListener.class);
	
	@RabbitListener(queues = {RabbitConfig.MESSAGE_QUEUE})
	public void listenerAutoAck(Book book, Message message, Channel channel) {
		final long deliveryTag = message.getMessageProperties().getDeliveryTag();
		try {
			logger.info("[listenerAutoAck 监听的消息] - [{}]", book.toString());
			// TODO: 2020/10/15 消息处理
			channel.basicAck(deliveryTag, false);
		} catch (IOException e) {
			try {
				channel.basicRecover();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
