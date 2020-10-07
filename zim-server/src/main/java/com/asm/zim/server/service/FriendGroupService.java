package com.asm.zim.server.service;

import com.asm.zim.common.constants.Result;
import com.asm.zim.server.entry.Friend;
import com.asm.zim.server.entry.FriendGroup;

import java.util.List;

/**
 * @author : azhao
 * @description
 */
public interface FriendGroupService {
	
	
	/**
	 * 删除分组
	 *
	 * @param personId
	 * @param groupId
	 * @return
	 */
	Result<String> removeFriendGroup(String personId, String groupId);
	
	/**
	 * 查看某个的人所有分组
	 *
	 * @param personId
	 * @return
	 */
	List<FriendGroup> findPersonGroup(String personId);
	
	/**
	 * 查看组下所有盆友信息  groupId 为空则查询所有
	 *
	 * @param personId
	 * @param groupId
	 * @return
	 */
	List<Friend> findGroupFriend(String personId, String groupId);
	
	
	/**
	 * 添加好友分组
	 *
	 * @param personId
	 * @param name
	 * @return
	 */
	FriendGroup addPersonGroup(String personId, String name);
}
