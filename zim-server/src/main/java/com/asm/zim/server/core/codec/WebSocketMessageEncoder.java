package com.asm.zim.server.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description WebSocket编码
 * @Author azhao
 */
public class WebSocketMessageEncoder extends MessageToMessageEncoder<Object> {
	private Logger logger = LoggerFactory.getLogger(WebSocketMessageEncoder.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
		logger.info("WebSocketMessageEncoder");

		/*if (frame instanceof BinaryWebSocketFrame) {
			logger.info("二进制数据帧为 BinaryWebSocketFrame");
			BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
			out.add(Unpooled.copiedBuffer(binaryWebSocketFrame.content()));
		}*/
		if (msg instanceof FullHttpResponse) {
			logger.info("FullHttpResponse");
			out.add(((FullHttpResponse) msg).retain());
		}
		if (msg instanceof ByteBuf) {
			logger.info("byteBuf");
			WebSocketFrame frame = new BinaryWebSocketFrame((ByteBuf) msg);
			out.add(frame);
		}
		//out.add(new BinaryWebSocketFrame(byteBuf));
		/*if (msg instanceof MessageLite) {
			ByteBuf buf = Unpooled.wrappedBuffer(((MessageLite) msg).toByteArray());
			BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buf);
			out.add(frame);
		} else if (msg instanceof MessageLite.Builder) {
			ByteBuf buf = Unpooled.wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
			BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buf);
			out.add(frame);
		}*/
	}
}
