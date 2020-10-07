package com.asm.zim.server.core.route.redis;

import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.core.container.LocalChannelGroup;
import com.asm.zim.server.entry.ChannelReceiveCommand;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author : azhao
 * @description channel 路由监听
 */
@Component
public class RouteChannel {
	private Logger logger = LoggerFactory.getLogger(RouteChannel.class);
	@Autowired
	private LocalChannelGroup socketChannelGroup;
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
	public void receiveMessage(String crcJson) {
		Gson gson = new Gson();
		ChannelReceiveCommand crc = gson.fromJson(crcJson, ChannelReceiveCommand.class);
		if (crc.getCmd().equals("removeChannel")) {
			String token = (String) crc.getData();
			if (StringUtils.isEmpty(token)) {
				socketChannelGroup.clearChannel();
				logger.info("集群 channel 被清空");
			} else {
				token = token.substring(1, token.length() - 1);
				socketChannelGroup.removeChannel(token);
				logger.info("token {}移除 ", token);
			}
		}
		
		if (crc.getCmd().equals("getChannelTokens")) {
			String equipmentId = (String) crc.getData();
			if (Constants.EQUIPMENT_ID.equals(equipmentId)) {
				List<String> tokens = socketChannelGroup.getChannelToken();
				Map<String, Channel> channelMap = socketChannelGroup.getChannelMap();
				logger.info(String.valueOf(channelMap));
				for (String s : tokens) {
					redisTemplate.opsForSet().add(Constants.CURRENT_IP_TOKEN_PREFIX + equipmentId, s);
				}
			}
		}
	}
}
