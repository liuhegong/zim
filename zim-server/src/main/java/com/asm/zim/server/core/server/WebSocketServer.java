package com.asm.zim.server.core.server;

import com.asm.zim.server.config.net.WebSocketArgsConfig;
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
@Component("webSocketServer")
public class WebSocketServer {
	private Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
	@Resource(name = "webSocketBossGroup")
	private EventLoopGroup bossGroup;
	@Resource(name = "webSocketWorkGroup")
	private EventLoopGroup workerGroup;
	@Resource(name = "webSocketServerBootstrap")
	private ServerBootstrap bootstrap;
	private int port;
	@Resource(name = "webSocketArgsConfig")
	private WebSocketArgsConfig webSocketArgsConfig;
	private ChannelFuture cf = null;
	@Value("${im.websocket.start}")
	private String hasStart = "false";
	
	@PostConstruct
	public void start() {
		if (!Boolean.parseBoolean(hasStart)) {
			return;
		}
		new Thread(() -> {
			try {
				port = webSocketArgsConfig.getPort();
				cf = bootstrap.bind(port).sync();
				cf.addListener((ChannelFutureListener) future -> {
					if (cf.isSuccess()) {
						logger.info("webSocket 监听端口 {} 成功", port);
					} else {
						logger.info("webSocket 监听端口 {} 失败", port);
					}
				});
				Future future = cf.channel().closeFuture();
				if (future.isSuccess()) {
					logger.info("webSocket 服务关闭成功");
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
			logger.info("webSocket 端口 {} 关闭", port);
		}
	}
	
}
