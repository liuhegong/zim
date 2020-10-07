package com.asm.zim.server.core.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description WebSocket解码
 * @Author azhao
 */
public class WebSocketMessageDecoder extends MessageToMessageDecoder<WebSocketFrame> {
	private Logger logger = LoggerFactory.getLogger(WebSocketMessageDecoder.class);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> list) throws Exception {
		if (frame instanceof TextWebSocketFrame) {
			logger.info("文本帧消息：" + ((TextWebSocketFrame) frame).text());
		} else if (frame instanceof BinaryWebSocketFrame) {
			logger.info("二进制数据帧为 BinaryWebSocketFrame");
			BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
			list.add(Unpooled.copiedBuffer(binaryWebSocketFrame.content()));
		} else if (frame instanceof PingWebSocketFrame) {
			logger.info("PingWebSocketFrame 处理");
		}
	}
}
