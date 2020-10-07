package com.asm.zim.server.core.chat;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.constants.ChatType;
import com.asm.zim.common.constants.MessageCode;
import com.asm.zim.common.constants.MessageType;
import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.core.container.LocalChannelGroup;
import com.asm.zim.server.core.route.RouteMessage;
import com.asm.zim.server.entry.Friend;
import com.asm.zim.server.service.FriendGroupService;
import com.asm.zim.server.service.TokenService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author : azhao
 * @description
 */
@Service
public class SystemChatService {
	private Logger logger = LoggerFactory.getLogger(SystemChatService.class);
	@Autowired
	private LocalChannelGroup socketChannelGroup;
	@Autowired
	private FriendGroupService friendGroupService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RouteMessage routeMessage;
	/**
	 * 消息确认
	 *
	 * @param msg
	 */
	public void systemConfirmMessage(BaseMessage.Message msg) {
		BaseMessage.Message.Builder builder = msg.toBuilder();
		builder.setCode(MessageCode.ReplySuccess);
		builder.setMessageType(MessageType.System);
		builder.setToId(msg.getFromId());
		msg = builder.build();
		logger.info("发送系统消息确认");
		routeMessage.sendMessage(msg);
	}
	
	/**
	 * 心跳包
	 */
	public void heartMessage(BaseMessage.Message msg) {
		BaseMessage.Message.Builder builder = msg.toBuilder();
		builder.setCode(MessageCode.Heart);
		builder.setMessageType(MessageType.System);
		builder.setToId(msg.getFromId());
		msg = builder.build();
		routeMessage.receiveMessage(msg);
	}
	
	/**
	 * 通知上下线
	 *
	 * @param personId  上下线通知人
	 * @param hasOnLine true 为上线,false 为下线
	 */
	public void systemLineMessage(String personId, String protocol, boolean hasOnLine) {
		List<Friend> friendList = friendGroupService.findGroupFriend(personId, null);
		BaseMessage.Message.Builder builder = BaseMessage.Message.newBuilder();
		if (hasOnLine) {
			builder.setCode(MessageCode.OnlineNotice);
		} else {
			builder.setCode(MessageCode.OffLineNotice);
		}
		if (protocol != null) {
			builder.setProtocol(protocol);
		}
		builder.setFromId(personId);
		builder.setMessageType(MessageType.System);
		for (Friend friend : friendList) {
			String id = friend.getFriendId();
			builder.setToId(id);
			builder.setSendTime(System.currentTimeMillis());
			routeMessage.sendMessage(builder.build());
		}
	}
	
	/**
	 * 添加好友请求
	 *
	 * @param msg
	 */
	public void sendFriendRequest(BaseMessage.Message msg) {
		BaseMessage.Message.Builder builder = msg.toBuilder();
		builder.setCode(MessageCode.FriendRequest);
		msg = builder.build();
		routeMessage.receiveMessage(msg);
	}
	
	public void systemLoginResult(ChannelHandlerContext ctx, boolean hasSuccess, String protocol, String personId) {
		BaseMessage.Message.Builder builder = BaseMessage.Message.newBuilder();
		builder.setMessageType(MessageType.System);
		if (hasSuccess) {
			builder.setCode(MessageCode.LoginSuccess);
		} else {
			builder.setCode(MessageCode.LoginFail);
		}
		builder.setId(IdUtil.fastSimpleUUID());
		builder.setSendTime(System.currentTimeMillis());
		builder.setProtocol(protocol);
		builder.setToId(personId);
		BaseMessage.Message msg = builder.build();
		if (!hasSuccess) {
			ctx.pipeline().writeAndFlush(msg);
		} else {
			routeMessage.receiveMessage(msg);
		}
	}
	
	
	public boolean login(ChannelHandlerContext ctx, BaseMessage.Message msg) {
		String token = msg.getToken();
		TokenAuth tokenAuth = tokenService.getTokenAuth(token);
		if (tokenAuth == null) {
			logger.info("socket 登录失败 token不存在~");
			systemLoginResult(ctx, false, msg.getProtocol(), msg.getFromId());
			return false;
		}
		if (!tokenAuth.getPersonId().equals(msg.getFromId())) {
			systemLoginResult(ctx, false, msg.getProtocol(), msg.getFromId());
			logger.info("socket 登录失败 personId不一致~");
			return false;
		}
		String personId = msg.getFromId();
		ChannelPipeline pipeline = ctx.pipeline();
		Channel channel = pipeline.channel();
		String protocol = msg.getProtocol();
		socketChannelGroup.bindChannel(token, channel);
		systemLineMessage(personId, protocol, true);
		systemLoginResult(ctx, true, protocol, msg.getFromId());
		logger.info("socket 登录成功~");
		return true;
	}
	
	/**
	 * 账号通知退出登录
	 *
	 * @param token
	 */
	public void loginOut(String token, int code) {
		TokenAuth tokenAuth = tokenService.getTokenAuth(token);
		if (tokenAuth != null) {
			NetMessage netMessage = new NetMessage();
			netMessage.setId(IdUtil.fastSimpleUUID());
			netMessage.setCode(code);
			netMessage.setToken(token);
			netMessage.setMessageType(MessageType.System);
			netMessage.setChatType(ChatType.privateChat);
			netMessage.setFromId(tokenAuth.getPersonId());
			routeMessage.sendMessage(netMessage);
		}
	}
}
