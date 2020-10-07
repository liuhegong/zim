package com.asm.zim.server.config.net;

import com.asm.zim.server.core.handler.websocket.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description
 */
@Configuration
public class WebSocketNettyConfig implements ITcpNettyConfig {
	@Resource(name = "webSocketArgsConfig")
	private WebSocketArgsConfig webSocketArgsConfig;
	@Resource(name = "webSocketChannelInitializer")
	private WebSocketChannelInitializer webSocketChannelInitializer;
	
	@Bean(name = "webSocketBossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(webSocketArgsConfig.getBossThreadCount());
	}
	
	@Bean(name = "webSocketWorkGroup", destroyMethod = "shutdownGracefully")
	@Override
	public NioEventLoopGroup workGroup() {
		return new NioEventLoopGroup(webSocketArgsConfig.getWorkThreadCount());
	}
	
	@Bean(name = "webSocketServerBootstrap")
	public ServerBootstrap serverBootstrap() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup(), workGroup())
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, webSocketArgsConfig.getQueueCount())
				.option(ChannelOption.SO_KEEPALIVE, webSocketArgsConfig.isKeepalive())
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(webSocketChannelInitializer);
		return bootstrap;
	}
}
