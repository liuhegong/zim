package com.asm.zim.server.core.route.netty;

import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.config.yaml.TcpConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : azhao
 * @description
 */
@Service
public class RouteClient {
	private Logger logger = LoggerFactory.getLogger(RouteClient.class);
	@Autowired
	private TcpConfig tcpConfig;
	private Bootstrap bootstrap = new Bootstrap();
	private EventLoopGroup group = new NioEventLoopGroup();
	/**
	 * ip
	 * channel
	 */
	private Map<String, Channel> channelMap = new ConcurrentHashMap<>();
	
	public Channel getChannel(String ip) {
		if (StringUtils.isEmpty(ip)) {
			return null;
		}
		Channel channel = channelMap.get(ip);
		if (channel == null) {
			logger.info("建立连接中...");
			connect(ip, tcpConfig.getPort());
			channel = channelMap.get(ip);
		}
		return channel;
	}
	
	@PostConstruct
	public void initSocket() {
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						pipeline.addLast(new ProtobufEncoder());
						pipeline.addLast(new ProtobufDecoder(BaseMessage.Message.getDefaultInstance()));
						pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
						pipeline.addLast(new SimpleChannelInboundHandler<BaseMessage.Message>() {
							@Override
							protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMessage.Message message) throws Exception {
								String content = message.getContent();
								logger.info(content);
							}
						});
					}
				});
	}
	
	@PreDestroy
	public void stop() {
		group.shutdownGracefully();
	}
	
	private void connect(String ip, int port) {
		try {
			ChannelFuture future = bootstrap.connect(ip, port).sync();
			Channel channel = future.channel();
			if (future.isSuccess()) {
				channelMap.put(ip, channel);
				logger.info("客户端{}:{}建立成功", ip, port);
			} else {
				logger.info("客户端{}:{}建立失败", ip, port);
			/*future.channel().eventLoop().schedule(new Runnable() {
				@Override
				public void run() {
					connect(ip, port);
				}
			}, 5, TimeUnit.SECONDS);*/
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
