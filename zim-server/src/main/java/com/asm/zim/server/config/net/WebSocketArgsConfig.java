package com.asm.zim.server.config.net;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : azhao
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component("webSocketArgsConfig")
@ConfigurationProperties(prefix = "im.websocket")
public class WebSocketArgsConfig extends ArgsConfig {
	private int port = 8888;
	private int maxFrameAggregator = 10 * 1024 * 1024;
	private int frameMaxLength = 100 * 1024 * 1024;
	private int httpMaxLength = 512 * 1024;
}
