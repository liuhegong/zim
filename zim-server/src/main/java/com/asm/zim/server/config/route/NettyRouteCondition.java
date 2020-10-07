package com.asm.zim.server.config.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author : azhao
 * @description netty 直连路由
 */
public class NettyRouteCondition implements Condition {
	private Logger logger = LoggerFactory.getLogger(NettyRouteCondition.class);
	
	@Override
	public boolean matches(ConditionContext cc, AnnotatedTypeMetadata atm) {
		Environment environment = cc.getEnvironment();
		String property = environment.getProperty("im.route-way");
		return "netty".equals(property);
	}
}
