package com.asm.zim.server.config.yaml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author : azhao
 */
@Component
@ConfigurationProperties(prefix = "im.websocket")
@Data
@Validated
public class WebsocketConfig {
	private boolean start = true;
	private int port = 8888;
	private int bossThreadCount = 1;
	private int workThreadCount = 8;
	private int queueCount = 128;
	private boolean keepalive = true;
	private int httpMaxLength = 512 * 1024;
	private int maxFrameAggregator = 10 * 1024 * 1024;
	private int frameMaxLength = 100 * 1024 * 1024;
}
