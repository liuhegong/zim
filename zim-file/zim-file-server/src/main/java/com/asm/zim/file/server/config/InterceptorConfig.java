package com.asm.zim.file.server.config;

import com.asm.zim.file.server.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author : azhao
 * @description
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
	@Autowired
	private LoginFilter loginFilter;
	
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginFilter)
				.addPathPatterns("/**");
	}
}
