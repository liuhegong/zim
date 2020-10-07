package com.asm.zim.server.config;

import com.asm.zim.server.filter.LoginInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {
	private Logger logger = LoggerFactory.getLogger(FilterConfig.class);
	@Autowired
	private LoginInterceptor loginInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		logger.info("filterConfig loginInterceptor");
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/register")
				.excludePathPatterns("/login")
				.excludePathPatterns("/loginOut")
				.excludePathPatterns("/static/**");
	}
}