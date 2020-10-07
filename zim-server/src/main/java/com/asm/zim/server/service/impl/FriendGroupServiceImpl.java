package com.asm.zim.server.service.impl;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.constants.Result;
import com.asm.zim.server.dao.FriendDao;
import com.asm.zim.server.dao.FriendGroupDao;
import com.asm.zim.server.entry.Friend;
import com.asm.zim.server.entry.FriendGroup;
import com.asm.zim.server.service.FriendGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : azhao
 * @description
 */
@Service
public class FriendGroupServiceImpl implements FriendGroupService {
	private Logger logger = LoggerFactory.getLogger(FriendGroupServiceImpl.class);
	
	@Autowired
	private FriendDao friendDao;
	
	@Autowired
	private FriendGroupDao friendGroupDao;
	
	@Override
	public Result<String> removeFriendGroup(String personId, String groupId) {
		List<Friend> friendList = friendDao.selectGroupFriend(personId, groupId);
		if (friendList != null && friendList.size() > 0) {
			return new Result<String>().failure("组下有好友不能删除");
		}
		friendGroupDao.deleteById(groupId);
		logger.info("personId :{} groupId: {}组下好友移除成功",personId,groupId);
		return new Result<String>().success();
	}
	
	@Override
	public List<FriendGroup> findPersonGroup(String personId) {
		QueryWrapper<FriendGroup> wrapper = new QueryWrapper<>();
		wrapper.eq("person_id", personId);
		return friendGroupDao.selectList(wrapper);
	}
	
	@Override
	public List<Friend> findGroupFriend(String personId, String groupId) {
		return friendDao.selectGroupFriend(personId, groupId);
	}
	
	
	@Override
	public FriendGroup addPersonGroup(String personId, String name) {
		FriendGroup friendGroup = new FriendGroup();
		friendGroup.setId(IdUtil.fastSimpleUUID());
		friendGroup.setPersonId(personId);
		friendGroup.setName(name);
		friendGroupDao.insert(friendGroup);
		return friendGroup;
	}
}
