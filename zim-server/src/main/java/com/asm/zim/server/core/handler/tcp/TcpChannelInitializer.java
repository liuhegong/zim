package com.asm.zim.server.core.handler.tcp;

import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.core.handler.DistributeHandler;
import com.asm.zim.server.core.handler.LoginHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description
 */
@Component(value = "tcpChannelInitializer")
public class TcpChannelInitializer extends ChannelInitializer<SocketChannel> {
	private Logger logger = LoggerFactory.getLogger(TcpChannelInitializer.class);
	@Resource(name = "loginHandler")
	private LoginHandler loginHandler;
	
	@Autowired
	private DistributeHandler distributeHandler;
	
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		logger.info("tcpChannelInitializer initChannel");
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast(new ProtobufEncoder());
		pipeline.addLast(new ProtobufDecoder(BaseMessage.Message.getDefaultInstance()));
		pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("loginHandler", loginHandler);
		pipeline.addLast("distributeHandler", distributeHandler);
	}
}
