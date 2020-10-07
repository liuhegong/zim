package com.asm.zim.server.service;

import com.asm.zim.common.constants.Result;
import com.asm.zim.server.entry.Friend;

/**
 * @author : azhao
 * @description
 */
public interface FriendService {
	void add(Friend friend);
	
	/**
	 * 查看好友基本信息
	 *
	 * @param personId 自己的id
	 * @param friendId 好友id
	 * @return
	 */
	Friend find(String personId, String friendId);
	
	/**
	 * 还有分组移动
	 *
	 * @param friendId
	 * @param groupId
	 */
	Result<String> move(String personId, String friendId, String groupId);
	
	/**
	 * 删除好友
	 *
	 * @param personId
	 * @param friendId
	 */
	void remove(String personId, String friendId);
}
