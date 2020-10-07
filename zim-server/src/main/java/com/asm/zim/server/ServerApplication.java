package com.asm.zim.server;

import com.asm.zim.server.core.server.TcpServer;
import com.asm.zim.server.core.server.WebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties
@MapperScan("com.asm.zim.server.dao")
public class ServerApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ServerApplication.class, args);
		TcpServer tcpServer = applicationContext.getBean(TcpServer.class);
		tcpServer.start();
		WebSocketServer webSocketServer = applicationContext.getBean(WebSocketServer.class);
		webSocketServer.start();
	}
}
