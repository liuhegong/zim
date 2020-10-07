package com.asm.zim.server.core.container;

import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.entry.ChannelReceiveCommand;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : azhao
 * @description 集群channel 容器服务
 */
@Component
public class ChannelGroup {
	private Logger logger = LoggerFactory.getLogger(ChannelGroup.class);
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	@Autowired
	private LocalChannelGroup localChannelGroup;
	
	/**
	 * 移除集群的channel
	 *
	 * @param token token 为 null 则 清除所有
	 */
	public void removeChannel(String token) {
		logger.info("token {}", token);
		ChannelReceiveCommand crc = new ChannelReceiveCommand("removeChannel", token);
		redisTemplate.convertAndSend(Constants.REDIS_CHANNEL_TOPIC, crc);
	}
	
	/**
	 * 获取指定设备Id 的token 列表 ，ip 为空随机获取tokens
	 */
	public Map<String, Object> getChannelTokens(String equipmentId) {
		if (StringUtils.isEmpty(equipmentId)) {
			logger.info("equipmentId 为空");
			List<String> tokens = localChannelGroup.getChannelToken();
			Map<String, String> tokenChannelMap = new HashMap<>();
			for (String token : tokens) {
				Channel channel = localChannelGroup.getChannelByToken(token);
				if (channel != null) {
					tokenChannelMap.put(token, localChannelGroup.getChannelId(channel));
				} else {
					tokenChannelMap.put(token, null);
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("equipmentId", Constants.EQUIPMENT_ID);
			map.put("tokenChannelId", tokenChannelMap);
			return map;
		}
		logger.info("getChannelTokens equipmentId {}", equipmentId);
		ChannelReceiveCommand crc = new ChannelReceiveCommand("getChannelTokens", equipmentId);
		redisTemplate.convertAndSend(Constants.REDIS_CHANNEL_TOPIC, crc);
		return null;
	}
}
