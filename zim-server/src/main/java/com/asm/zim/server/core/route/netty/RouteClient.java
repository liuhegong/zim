package com.asm.zim.server.core.route.netty;

import com.asm.zim.common.proto.BaseMessage;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : azhao
 * @description
 */
@Service
public class RouteClient {
	private Logger logger = LoggerFactory.getLogger(RouteClient.class);
	
	@Value("${im.tcp.port}")
	private String serverPort = "7777";
	/**
	 * ip
	 * channel
	 */
	
	private ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
	
	public Channel getChannel(String ip) {
		if (StringUtils.isEmpty(ip)) {
			return null;
		}
		Channel channel = channelMap.get(ip);
		if (channel == null) {
			logger.info("建立连接中...");
			connect(ip, Integer.parseInt(serverPort));
			channel = channelMap.get(ip);
		}
		return channel;
	}
	
	private void connect(String ip, int port) {
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
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
			logger.info("客户端 ok..");
			ChannelFuture future = bootstrap.connect(ip, port);
			future.sync();
			Channel channel = future.channel();
			if (future.isSuccess()) {
				channelMap.put(ip, channel);
				logger.info("客户端{}:{}建立成功", ip, port);
			} else {
				logger.info("客户端{}:{}建立失败", ip, port);
			}
			ChannelFuture closeFuture = future.channel().closeFuture();
			if (closeFuture.isSuccess()) {
				logger.info("客户端{}:{}关闭成功", ip, port);
			} else {
				logger.info("客户端{}:{}关闭失败", ip, port);
			}
			closeFuture.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
