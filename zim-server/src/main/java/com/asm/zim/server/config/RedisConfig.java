package com.asm.zim.server.config;

import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.core.container.RedisKeyExpirationListener;
import com.asm.zim.server.core.route.redis.RouteChannel;
import com.asm.zim.server.core.route.redis.RouteMessageReceiver;
import com.asm.zim.server.core.route.redis.RouteMessageSend;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig extends CachingConfigurerSupport {
	private Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Bean
	public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Serializable> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		logger.info("redisTemplate 加载完毕");
		return template;
	}
	
	@Bean
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append(obj.toString());
			}
			return sb.toString();
		};
	}
	
	@Bean
	public CacheManager cacheManager(LettuceConnectionFactory factory) {
		RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(factory);
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
		return new RedisCacheManager(writer, config);
	}
	
	/**
	 * 禁用序列化的问题
	 *
	 * @return
	 */
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}
	
	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory
			, MessageListenerAdapter listenerRouteMessageReceiver
			, MessageListenerAdapter listenerRouteMessageSend
			, MessageListenerAdapter listenerRouteMessageSendByToken
			, MessageListenerAdapter listenerRouteChannelReceiver
	) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// 可以添加多个 messageListener，配置不同的交换机 类型是 MessageListenerAdapter
		container.addMessageListener(listenerRouteMessageReceiver, new PatternTopic(Constants.REDIS_MESSAGE_RECEIVE_TOPIC));
		container.addMessageListener(listenerRouteMessageSend, new PatternTopic(Constants.REDIS_MESSAGE_SEND_TOPIC));
		container.addMessageListener(listenerRouteMessageSendByToken, new PatternTopic(Constants.REDIS_MESSAGE_SEND_BY_TOKEN_TOPIC));
		container.addMessageListener(listenerRouteChannelReceiver, new PatternTopic(Constants.REDIS_CHANNEL_TOPIC));
		return container;
	}
	
	@Bean
	public MessageListenerAdapter listenerRouteMessageReceiver(RouteMessageReceiver routeMessageReceiver) {
		return new MessageListenerAdapter(routeMessageReceiver, "receiveMessage");
	}
	
	@Bean
	public MessageListenerAdapter listenerRouteMessageSend(RouteMessageSend routeMessageSend) {
		return new MessageListenerAdapter(routeMessageSend, "sendMessage");
	}
	@Bean
	public MessageListenerAdapter listenerRouteMessageSendByToken(RouteMessageSend routeMessageSend) {
		return new MessageListenerAdapter(routeMessageSend, "sendMessageByToken");
	}
	
	@Bean
	public MessageListenerAdapter listenerRouteChannelReceiver(RouteChannel routeChannel) {
		return new MessageListenerAdapter(routeChannel, "receiveMessage");
	}
	
	
	@Bean
	public RedisKeyExpirationListener keyExpirationListener(RedisMessageListenerContainer listenerContainer) {
		return new RedisKeyExpirationListener(listenerContainer);
	}
}
