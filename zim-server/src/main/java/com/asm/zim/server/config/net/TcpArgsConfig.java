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
@Component("tcpArgsConfig")
@ConfigurationProperties(prefix = "im.tcp")
public class TcpArgsConfig extends ArgsConfig {
	private int port = 7777;
}
