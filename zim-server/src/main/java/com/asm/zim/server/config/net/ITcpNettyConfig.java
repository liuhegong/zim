package com.asm.zim.server.config.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author : azhao
 * @description
 */
public interface ITcpNettyConfig {
	NioEventLoopGroup bossGroup();
	
	NioEventLoopGroup workGroup();
	
	ServerBootstrap serverBootstrap();
}
