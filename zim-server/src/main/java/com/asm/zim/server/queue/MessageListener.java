package com.asm.zim.server.queue;

import com.asm.zim.server.config.queue.RabbitConfig;
import com.asm.zim.server.entry.Message;
import com.asm.zim.server.service.MessageService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author : azhao
 * @description
 */
@Component
public class MessageListener {
	private Logger logger = LoggerFactory.getLogger(MessageListener.class);
	@Autowired
	private MessageService messageService;
	
	/*
	bindings = @QueueBinding(
			value = @Queue(name = RabbitConfig.SEND_MESSAGE_QUEUE, durable = "true"),
			exchange = @Exchange(name =RabbitConfig.MESSAGE_EXCHANGE
			)),
	 */
	@RabbitListener(queues = RabbitConfig.SEND_MESSAGE_QUEUE)
	public void saveSend(Message message, org.springframework.amqp.core.Message mqMessage, Channel channel) {
		final long deliveryTag = mqMessage.getMessageProperties().getDeliveryTag();
		try {
			logger.debug("saveSend 消费消息 {}", message);
			messageService.saveSend(message);
			channel.basicAck(deliveryTag, false);
		} catch (IOException e) {
			try {
				channel.basicRecover();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@RabbitListener(queues = RabbitConfig.RECEIVE_MESSAGE_QUEUE)
	public void saveReceive(Message message, org.springframework.amqp.core.Message mqMessage, Channel channel) {
		final long deliveryTag = mqMessage.getMessageProperties().getDeliveryTag();
		try {
			logger.debug("saveReceive 消费消息 {}", message);
			messageService.saveReceive(message);
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
