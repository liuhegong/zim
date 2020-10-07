package com.asm.zim.server.core.server;

import com.asm.zim.server.config.net.TcpArgsConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author : azhao
 * @description
 */
@Component
public class TcpServer {
	private Logger logger = LoggerFactory.getLogger(TcpServer.class);
	@Resource(name = "tcpBossGroup")
	private EventLoopGroup bossGroup;
	@Resource(name = "tcpWorkGroup")
	private EventLoopGroup workerGroup;
	@Resource(name = "tcpServerBootstrap")
	private ServerBootstrap bootstrap;
	private ChannelFuture cf;
	private int port;
	@Resource(name = "tcpArgsConfig")
	private TcpArgsConfig tcpArgsConfig;
	@Value("${im.tcp.start}")
	private String hasStart = "false";
	
	@PostConstruct
	public void start() {
		if (!Boolean.parseBoolean(hasStart)) {
			return;
		}
		new Thread(() -> {
			port = tcpArgsConfig.getPort();
			try {
				cf = bootstrap.bind(port).sync();
				cf.addListener((ChannelFutureListener) future -> {
					if (cf.isSuccess()) {
						logger.info("Tcp 监听端口 {} 成功", port);
					} else {
						logger.info("Tcp 监听端口 {} 失败", port);
					}
				});
				Future future = cf.channel().closeFuture();
				if (future.isSuccess()) {
					logger.info("Tcp 服务关闭成功");
				}
				future.sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	@PreDestroy
	public void destroy() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		if (Boolean.parseBoolean(hasStart)) {
			logger.info("tcp 端口 {} 关闭", port);
		}
	}
}
