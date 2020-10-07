package com.asm.zim.server.core.container;

import com.asm.zim.server.entry.Friend;
import com.asm.zim.server.service.FriendGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : azhao
 * @description
 */
@Service
public class OnlineManager {
	private Logger logger = LoggerFactory.getLogger(OnlineManager.class);
	@Autowired
	private FriendGroupService friendGroupService;
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
	public List<String> getOnlinePersonIds(String personId, String groupId) {
		logger.info("获取在线 personId {} groupId {}的personIds", personId, groupId);
		List<Friend> friendList = friendGroupService.findGroupFriend(personId, groupId);
		List<String> idList = friendList.stream().map(Friend::getFriendId).collect(Collectors.toList());
		List<String> onLineIdList = new LinkedList<>();
		for (String id : idList) {
			if (redisTemplate.opsForValue().get(id) != null) {
				onLineIdList.add(id);
			}
		}
		return onLineIdList;
	}
}
