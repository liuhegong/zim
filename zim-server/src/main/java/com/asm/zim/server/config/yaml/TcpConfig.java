package com.asm.zim.server.config.yaml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author : azhao
 */
@Component
@ConfigurationProperties(prefix = "im.tcp")
@Data
@Validated
public class TcpConfig {
	private boolean start = true;
	private int port = 7777;
	private int bossThreadCount = 1;
	private int workThreadCount = 8;
	private int queueCount = 128;
	private boolean keepalive = true;
}
