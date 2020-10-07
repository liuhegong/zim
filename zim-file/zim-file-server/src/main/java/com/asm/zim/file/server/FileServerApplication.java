package com.asm.zim.file.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@MapperScan("com.asm.zim.file.server.dao")
@SpringBootApplication
public class FileServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FileServerApplication.class, args);
	}
}
