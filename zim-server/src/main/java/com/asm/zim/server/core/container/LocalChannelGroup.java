package com.asm.zim.server.core.container;

import com.asm.zim.server.service.TokenService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : azhao
 * @description 连接的Channel 容器
 */
@Component
public class LocalChannelGroup {
	private Logger logger = LoggerFactory.getLogger(LocalChannelGroup.class);
	@Autowired
	private TokenService tokenService;
	/**
	 * token
	 * channel
	 */
	private ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
	/**
	 * channelId
	 * token
	 */
	private ConcurrentHashMap<String, String> channelIdMap = new ConcurrentHashMap<>();
	
	public Map<String, Channel> getChannelMap() {
		return channelMap;
	}
	
	
	public Map<String, String> getChannelIdMap() {
		return channelIdMap;
	}
	
	public String getChannelId(Channel channel) {
		String chanelId = channel.id().asShortText();
		logger.info("channelId asShortText 为 {}", chanelId);
		return chanelId;
	}
	
	/**
	 * 清空当前机器的channelMap
	 *
	 * @param token
	 */
	public void removeChannel(String token) {
		if (token != null) {
			Channel channel = channelMap.get(token);
			if (channel != null) {
				String channelId = getChannelId(channel);
				logger.info("token {}", token);
				channelIdMap.remove(channelId);
				channelMap.remove(token);
			}
		}
	}
	
	public void removeChannel(Channel channel) {
		if (channel == null) {
			return;
		}
		String channelId = getChannelId(channel);
		String token = channelIdMap.get(channelId);
		removeChannel(token);
	}
	
	
	/**
	 * 清空channel
	 */
	public void clearChannel() {
		channelMap.clear();
		channelIdMap.clear();
		logger.info("clearChannel");
	}
	
	/**
	 * 获取当前机器所有的token
	 *
	 * @return
	 */
	public List<String> getChannelToken() {
		return new ArrayList<>(channelMap.keySet());
	}
	
	/**
	 * 绑定 channel 的同时重定位token IP
	 *
	 * @param token
	 * @param channel
	 */
	public void bindChannel(String token, Channel channel) {
		if (!channelMap.containsKey(token)) {
			channelIdMap.put(getChannelId(channel), token);
		}
		channelMap.put(token, channel);
		tokenService.updateEquipmentIdByToken(token);
		logger.info("token {} ", token);
	}
	
	public Channel getChannelByPerson(String personId) {
		if (personId == null) {
			return null;
		}
		String token = tokenService.getToken(personId);
		logger.info("personId {} token {} ", personId, token);
		return getChannelByToken(token);
	}
	
	public Channel getChannelByToken(String token) {
		if (token == null) {
			return null;
		}
		logger.info("token {} ", token);
		return channelMap.get(token);
	}
}
