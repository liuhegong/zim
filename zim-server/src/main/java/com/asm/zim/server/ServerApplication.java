package com.asm.zim.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

// (exclude = {RabbitAutoConfiguration.class})
@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableRabbit
@EnableConfigurationProperties
@MapperScan("com.asm.zim.server.dao")
public class ServerApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ServerApplication.class, args);
	}
}
