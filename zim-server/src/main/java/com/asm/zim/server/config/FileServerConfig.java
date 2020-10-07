package com.asm.zim.server.config;

import com.asm.zim.file.client.api.FileManageService;
import com.asm.zim.file.client.api.impl.FileManageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : azhao
 * @description
 */
@Configuration
public class FileServerConfig {
	private Logger logger = LoggerFactory.getLogger(FileServerConfig.class);
	@Value("${im.file-server.url}")
	public String url;
	
	@Bean
	public FileManageService fileManageService() {
		logger.info("fileManagerService 初始化 url {} ", url);
		return new FileManageServiceImpl(url);
	}
}
