package com.asm.zim.server.config.route;

import com.asm.zim.server.common.constants.RouteWay;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author : azhao
 * @description netty 直连路由
 */
public class NettyRouteCondition implements Condition {
	
	@Override
	public boolean matches(ConditionContext cc, AnnotatedTypeMetadata atm) {
		Environment environment = cc.getEnvironment();
		String property = environment.getProperty("im.route-way");
		property = property == null ? RouteWay.NETTY : property;
		return RouteWay.NETTY.equals(property);
	}
}
