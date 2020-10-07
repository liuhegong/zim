package com.asm.zim.server.config.net;

import io.netty.util.NettyRuntime;
import lombok.Data;

/**
 * @author : azhao
 * @description
 */
@Data
public class ArgsConfig {
	private int port = 8888;
	private int bossThreadCount = 1;
	private int workThreadCount = NettyRuntime.availableProcessors();
	private int queueCount = 128;
	private boolean keepalive = true;
	private boolean start = true;
}
