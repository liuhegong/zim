package com.asm.zim.client.core;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.client.common.Common;
import com.asm.zim.client.service.LoginService;
import com.asm.zim.common.constants.*;
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

import java.util.Scanner;

import static com.asm.zim.client.common.Common.channel;
import static com.asm.zim.client.common.Common.personId;

/**
 * @author : azhao
 * @description
 */
public class TcpClient {
	private Logger logger = LoggerFactory.getLogger(TcpClient.class);
	private EventLoopGroup group = new NioEventLoopGroup();
	
	public void start(String ip, int port) {
		try {
			Bootstrap bootstrap = new Bootstrap();
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
								protected void channelRead0(ChannelHandlerContext ctx, BaseMessage.Message message) throws Exception {
									String content = message.getContent();
									logger.info(content);
								}
							});
						}
					});
			logger.info("客户端 ok..");
			ChannelFuture future = bootstrap.connect(ip, port);
			future.sync();
			if (future.isSuccess()) {
				logger.info("客户端{}:{}建立成功", ip, port);
				Common.channel = future.channel();
				LoginService loginService = new LoginService();
				boolean hasLogin = loginService.login("lisi", "1234");
				if (hasLogin) {
					loginService.tcpLogin();
					logger.info("请输入...");
					Scanner scanner = new Scanner(System.in);
					while (true) {
						String context = scanner.nextLine();
						if (context.equals("exit")) {
							return;
						}
						BaseMessage.Message.Builder builder = BaseMessage.Message.newBuilder();
						builder.setId(IdUtil.fastSimpleUUID());
						builder.setContent(context);
						builder.setChatType(ChatType.privateChat);
						builder.setMessageCategory(MessageCategory.Text);
						builder.setSendTime(System.currentTimeMillis());
						builder.setProtocol(NetProtocol.TCP);
						builder.setToken(Common.token);
						builder.setFromId(personId);
						builder.setToId("514cc43ba09a473e9bd1ea31ebedc004");//zhangsan
						builder.setTerminalType(TerminalType.Android.getValue());
						builder.setMessageType(MessageType.Ordinary);
						channel.writeAndFlush(builder.build());
					}
				}
			} else {
				logger.info("客户端{}:{}建立失败", ip, port);
			}
			future.channel().closeFuture().sync();
			/*ChannelFuture closeFuture = future.channel().closeFuture();
			if (closeFuture.isSuccess()) {
				logger.info("客户端{}:{}关闭成功", ip, port);
			} else {
				logger.info("客户端{}:{}关闭失败", ip, port);
			}
			closeFuture.sync();
			*/
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		TcpClient tcpClient = new TcpClient();
		tcpClient.start("127.0.0.1", 7777);
		//tcpClient.start("192.168.52.129", 10000);
	}
}
