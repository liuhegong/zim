package com.asm.zim.server.config.net;

import com.asm.zim.server.core.handler.tcp.TcpChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description
 */
@Configuration
@Component
public class TcpNettyConfig implements ITcpNettyConfig {
	@Resource(name = "tcpArgsConfig")
	private TcpArgsConfig tcpArgsConfig;
	@Resource(name = "tcpChannelInitializer")
	private TcpChannelInitializer tcpChannelInitializer;
	
	@Bean(name = "tcpBossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(tcpArgsConfig.getBossThreadCount());
	}
	
	@Bean(name = "tcpWorkGroup", destroyMethod = "shutdownGracefully")
	@Override
	public NioEventLoopGroup workGroup() {
		return new NioEventLoopGroup(tcpArgsConfig.getWorkThreadCount());
	}
	
	@Bean(name = "tcpServerBootstrap")
	public ServerBootstrap serverBootstrap() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup(), workGroup())
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, tcpArgsConfig.getQueueCount())
				.option(ChannelOption.SO_KEEPALIVE, tcpArgsConfig.isKeepalive())
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(tcpChannelInitializer);
		return bootstrap;
	}
	
	
}
