package com.asm.zim.server.config;

import com.asm.zim.file.client.api.FileManageService;
import com.asm.zim.file.client.api.impl.FileManageServiceImpl;
import com.asm.zim.server.config.yaml.FileServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : azhao
 * @description
 */
@Configuration
public class RemoteFileServerConfig {
	private Logger logger = LoggerFactory.getLogger(RemoteFileServerConfig.class);
	@Autowired
	private FileServerConfig fileServerConfig;
	
	@Bean
	public FileManageService fileManageService() {
		logger.info("fileManagerService 初始化 url {} ", fileServerConfig.getUrl());
		return new FileManageServiceImpl(fileServerConfig.getUrl());
	}
}
