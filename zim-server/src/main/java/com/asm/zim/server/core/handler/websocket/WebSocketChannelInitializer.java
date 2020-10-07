package com.asm.zim.server.core.handler.websocket;

import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.config.net.WebSocketArgsConfig;
import com.asm.zim.server.core.codec.WebSocketMessageDecoder;
import com.asm.zim.server.core.handler.DistributeHandler;
import com.asm.zim.server.core.handler.LoginHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description
 */
@Component("webSocketChannelInitializer")
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
	private Logger logger = LoggerFactory.getLogger(WebSocketChannelInitializer.class);
	
	@Resource(name = "loginHandler")
	private LoginHandler loginHandler;
	@Resource(name = "distributeHandler")
	private DistributeHandler distributeHandler;
	@Resource(name = "webSocketHandler")
	private WebSocketHandler webSocketHandler;
	@Resource(name = "webSocketArgsConfig")
	private WebSocketArgsConfig webSocketArgsConfig;
	
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		logger.info("webSocketChannel init Channel");
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(webSocketArgsConfig.getHttpMaxLength()));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new WebSocketFrameAggregator(webSocketArgsConfig.getMaxFrameAggregator()));
		pipeline.addLast(new WebSocketServerCompressionHandler());
		pipeline.addLast(new WebSocketServerProtocolHandler("/", null, true, webSocketArgsConfig.getFrameMaxLength()));
		pipeline.addLast(new WebSocketMessageDecoder());
		pipeline.addLast(new ProtobufDecoder(BaseMessage.Message.getDefaultInstance()));
		pipeline.addLast(new ProtobufEncoder());
		pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("loginHandler", loginHandler);
		pipeline.addLast("webSocketHandler", webSocketHandler);
		pipeline.addLast("distributeHandler", distributeHandler);
	}
}
