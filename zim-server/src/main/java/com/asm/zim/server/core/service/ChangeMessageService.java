package com.asm.zim.server.core.service;

import com.asm.zim.common.constants.ChatType;
import com.asm.zim.common.constants.MessageCode;
import com.asm.zim.common.constants.MessageType;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.core.chat.GroupChatService;
import com.asm.zim.server.core.chat.PrivateChatService;
import com.asm.zim.server.core.chat.SystemChatService;
import com.asm.zim.server.core.container.LocalChannelGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : azhao
 * @description 消息处理
 */
@Service("changeMessageService")
public class ChangeMessageService {
	private Logger logger = LoggerFactory.getLogger(ChangeMessageService.class);
	@Autowired
	private LocalChannelGroup socketChannelGroup;
	
	@Resource(name = "privateChatService")
	private PrivateChatService privateChatService;
	
	@Resource(name = "groupChatService")
	private GroupChatService groupChatService;
	
	@Autowired
	private SystemChatService systemChatService;
	@Autowired
	private SendMessageService sendMessageService;
	
	
	public void handleRead(BaseMessage.Message msg) {
		handleRead(null, msg);
	}
	
	public void handleRead(ChannelHandlerContext ctx, BaseMessage.Message msg) {
		logger.debug(msg.toString());
		if (msg.getMessageType() == MessageType.System) {
			if (msg.getCode() == MessageCode.FriendRequest) {
				logger.info("添加好友请求{}", msg.toString());
				systemChatService.sendFriendRequest(msg);
			} else if (msg.getCode() == MessageCode.Heart) {
				logger.info("心跳检测");
				systemChatService.heartMessage(msg);
			} else if (msg.getCode() == MessageCode.ReplySuccess) {
				logger.info("其他机器发来的应答消息 ");
				sendMessageService.sendByToken(msg);
			} else if (msg.getCode() == MessageCode.OnlineNotice || msg.getCode() == MessageCode.OffLineNotice) {
				logger.info("其他机器发来的应答消息 上线通知");
				sendMessageService.sendByToId(msg);
			}
		} else {
			if (msg.getChatType() == ChatType.privateChat) {
				logger.info("私聊消息{}", msg.toString());
				privateChatService.handleChatMessage(msg);
			} else if (msg.getChatType() == ChatType.groupChat) {
				logger.info("群聊消息{}", msg.toString());
				groupChatService.handleChatMessage(msg);
			}
		}
	}
	
	public void channelActive(ChannelHandlerContext ctx) {
		ChannelPipeline pipeline = ctx.pipeline();
		Channel channel = pipeline.channel();
		socketChannelGroup.getChannelId(channel);
		logger.info("channelId 为{} 建立连接", socketChannelGroup.getChannelId(channel));
	}
	
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ChannelPipeline pipeline = ctx.pipeline();
		Channel channel = pipeline.channel();
		ChannelHandler handler = pipeline.get("loginHandler");
		if (handler != null) {
			logger.info("移除channel channelId 为 {} ", socketChannelGroup.getChannelId(channel));
			socketChannelGroup.removeChannel(channel);
		}
		logger.info("断开连接");
	}
}
