package com.asm.zim.server.controller;

import com.asm.zim.common.constants.Result;
import com.asm.zim.server.entry.Friend;
import com.asm.zim.server.entry.FriendGroup;
import com.asm.zim.server.service.FriendGroupService;
import com.asm.zim.server.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : azhao
 * @description 个人分组和好友
 */
@RestController
@RequestMapping("/group")
public class FriendGroupController {
	private Logger logger = LoggerFactory.getLogger(FriendGroupController.class);
	@Autowired
	private FriendGroupService friendGroupService;
	
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(value = "addPersonGroup")
	public Result<FriendGroup> addPersonGroup(String name) {
		String personId = tokenService.getPersonId();
		FriendGroup friendGroup = friendGroupService.addPersonGroup(personId, name);
		return new Result<FriendGroup>().success(friendGroup);
	}
	
	@RequestMapping(value = "removeFriendGroup")
	public Result<String> removeFriendGroup(String groupId) {
		String personId = tokenService.getPersonId();
		return friendGroupService.removeFriendGroup(personId, groupId);
	}
	
	/**
	 * 查看组下所有好友
	 *
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "findGroupFriend")
	public Result<List<Friend>> findGroupFriend(String groupId) {
		String personId = tokenService.getPersonId();
		List<Friend> friendList = friendGroupService.findGroupFriend(personId, groupId);
		return new Result<List<Friend>>().success(friendList);
	}
	
	/**
	 * 查看个人分组
	 *
	 * @return
	 */
	@RequestMapping(value = "findPersonGroup")
	public Result<List<FriendGroup>> findPersonGroup() {
		String personId = tokenService.getPersonId();
		List<FriendGroup> groupList = friendGroupService.findPersonGroup(personId);
		return new Result<List<FriendGroup>>().success(groupList);
	}
}
