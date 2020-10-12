package com.asm.zim.server.config.net;

import com.asm.zim.server.config.yaml.WebsocketConfig;
import com.asm.zim.server.core.handler.websocket.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description
 */
@Configuration
public class WebSocketConfig implements ITcpNettyConfig {
	@Autowired
	private WebsocketConfig websocketConfig;
	@Resource(name = "webSocketChannelInitializer")
	private WebSocketChannelInitializer webSocketChannelInitializer;
	
	@Bean(name = "webSocketBossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(websocketConfig.getBossThreadCount());
	}
	
	@Bean(name = "webSocketWorkGroup", destroyMethod = "shutdownGracefully")
	@Override
	public NioEventLoopGroup workGroup() {
		return new NioEventLoopGroup(websocketConfig.getWorkThreadCount());
	}
	
	@Bean(name = "webSocketServerBootstrap")
	public ServerBootstrap serverBootstrap() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup(), workGroup())
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, websocketConfig.getQueueCount())
				.option(ChannelOption.SO_KEEPALIVE, websocketConfig.isKeepalive())
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(webSocketChannelInitializer);
		return bootstrap;
	}
}
