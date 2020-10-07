package com.asm.zim.server.config.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author : azhao
 * @description redis 路由实现
 */
public class RedisRouteCondition implements Condition {
	private Logger logger = LoggerFactory.getLogger(RedisRouteCondition.class);
	
	@Override
	public boolean matches(ConditionContext cc, AnnotatedTypeMetadata atm) {
		Environment environment = cc.getEnvironment();
		String property = environment.getProperty("im.route-way");
		return "redis".equals(property);
	}
}
