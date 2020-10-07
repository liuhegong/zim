package com.asm.zim.server.controller;

import com.asm.zim.common.constants.Result;
import com.asm.zim.server.entry.Friend;
import com.asm.zim.server.entry.FriendVerification;
import com.asm.zim.server.service.FriendService;
import com.asm.zim.server.service.FriendVerificationService;
import com.asm.zim.server.service.TokenService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : azhao
 * @description
 */
@RestController
@RequestMapping("/friend")
public class FriendController {
	private Logger logger = LoggerFactory.getLogger(FriendController.class);
	@Autowired
	private FriendVerificationService friendVerificationService;
	@Autowired
	private FriendService friendService;
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(value = "agree")
	public Result<String> agree(String id, String remarkName, String groupId) {
		friendVerificationService.agreeFriend(id, remarkName, groupId);
		return new Result<String>().success();
	}
	
	@RequestMapping(value = "refuse")
	public Result<String> refuse(String id, String content) {
		friendVerificationService.refuseFriend(id, content);
		return new Result<String>().success();
	}
	
	/**
	 * 添加好友通知
	 *
	 * @return
	 */
	@RequestMapping(value = "loadAddFriendNotice")
	public Result<IPage<FriendVerification>> loadAddFriendNotice(int page) {
		String personId = tokenService.getTokenAuth().getPersonId();
		IPage<FriendVerification> pageResult = friendVerificationService.load(page, personId, null);
		return new Result<IPage<FriendVerification>>().success(pageResult);
	}
	
	/**
	 * 判断好友是否存在
	 *
	 * @param friendId
	 * @return
	 */
	@RequestMapping(value = "exit")
	public Result<String> exit(String friendId) {
		String personId = tokenService.getTokenAuth().getPersonId();
		Friend hasFriend = friendService.find(personId, friendId);
		if (hasFriend != null) {
			return new Result<String>().failure("好友列表已经存在");
		}
		return new Result<String>().success();
	}
	
	/**
	 * 查看好友信息
	 *
	 * @param friendId
	 * @return
	 */
	@RequestMapping("find")
	public Result<Friend> find(String friendId) {
		String personId =tokenService.getTokenAuth().getPersonId();
		Friend friend = friendService.find(personId, friendId);
		return new Result<Friend>().success(friend);
	}
	
	@RequestMapping(value = "add")
	public Result<String> add(FriendVerification friendVerification) {
		if (friendVerification.getGroupId() == null) {
			return new Result<String>().failure("分组为空");
		}
		if (friendVerification.getPersonId() == null || friendVerification.getFromId() == null) {
			return new Result<String>().failure("添加人不能为空");
		}
		if (friendVerification.getToId() == null) {
			return new Result<String>().failure("添加好友为空");
		}
		String personId = tokenService.getPersonId();
		Friend hasFriend = friendService.find(personId, friendVerification.getToId());
		if (hasFriend != null) {
			return new Result<String>().failure("好友列表已经存在");
		}
		return friendVerificationService.send(friendVerification);
	}
	
	/**
	 * 移动好友分组
	 *
	 * @param friendId
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "move")
	public Result<String> move(String friendId, String groupId) {
		String personId = tokenService.getPersonId();
		return friendService.move(personId, friendId, groupId);
	}
	
	
	/**
	 * 修改好、友备注
	 *
	 * @return
	 */
	@RequestMapping(value = "modifyRemarkName")
	public Result<String> modifyRemarkName(String name) {
		String personId = tokenService.getPersonId();
		return new Result<String>().success();
	}
	
	/**
	 * 删除好友
	 *
	 * @return
	 */
	@RequestMapping(value = "remove")
	public Result<String> remove(String friendId) {
		String personId = tokenService.getPersonId();
		friendService.remove(personId, friendId);
		return new Result<String>().success();
	}
}
