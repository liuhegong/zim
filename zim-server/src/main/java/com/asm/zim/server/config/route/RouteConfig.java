package com.asm.zim.server.config.route;

import com.asm.zim.server.core.route.RouteMessage;
import com.asm.zim.server.core.route.netty.NettyRouteMessage;
import com.asm.zim.server.core.route.redis.RedisRouteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : azhao
 * @description 路由选择方式 redis 发布订阅或 netty 集群通知
 */
@Configuration
public class RouteConfig {
	private Logger logger = LoggerFactory.getLogger(RouteConfig.class);
	
	@Bean
	@Conditional(NettyRouteCondition.class)
	public RouteMessage redisRouteMessage() {
		logger.info("加载Netty 路由方式");
		return new NettyRouteMessage();
	}
	
	@Bean
	@Conditional(RedisRouteCondition.class)
	public RouteMessage nettyRouteMessage() {
		logger.info("加载redis 路由方式");
		return new RedisRouteMessage();
	}
}
