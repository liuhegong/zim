package com.asm.zim.server.service.impl;

import com.asm.zim.common.constants.Result;
import com.asm.zim.server.dao.FriendDao;
import com.asm.zim.server.dao.PersonDao;
import com.asm.zim.server.entry.Friend;
import com.asm.zim.server.entry.Person;
import com.asm.zim.server.service.FriendService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : azhao
 * @description
 */
@Service
public class FriendServiceImpl implements FriendService {
	private Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);
	@Autowired
	private FriendDao friendDao;
	@Autowired
	private PersonDao personDao;
	
	@Override
	public void add(Friend friend) {
		if (friend == null) {
			return;
		}
		friendDao.insert(friend);
	}
	
	@Override
	public Friend find(String personId, String friendId) {
		QueryWrapper<Friend> wrapper = new QueryWrapper<>();
		wrapper.eq("person_id", personId);
		wrapper.eq("friend_id", friendId);
		Friend friend = friendDao.selectOne(wrapper);
		if (friend == null) {
			return null;
		}
		Person person = personDao.selectById(friendId);
		friend.setPerson(person);
		return friend;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result<String> move(String personId, String friendId, String groupId) {
		QueryWrapper<Friend> wrapper = new QueryWrapper<>();
		wrapper.eq("person_id", personId);
		wrapper.eq("friend_id", friendId);
		Friend friend = friendDao.selectOne(wrapper);
		if (friend == null) {
			return new Result<String>().failure("好友不存在");
		}
		UpdateWrapper<Friend> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", friend.getId());
		friend.setFriendGroupId(groupId);
		if (friendDao.update(friend, updateWrapper) > 0) {
			return new Result<String>().success();
		}
		return new Result<String>().failure();
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void remove(String personId, String friendId) {
		if (personId == null || friendId == null) {
			return;
		}
		UpdateWrapper<Friend> fromWrapper = new UpdateWrapper<>();
		fromWrapper.eq("person_id", personId);
		fromWrapper.eq("friend_id", friendId);
		friendDao.delete(fromWrapper);
		UpdateWrapper<Friend> toWrapper = new UpdateWrapper<>();
		toWrapper.eq("person_id", friendId);
		toWrapper.eq("friend_id", personId);
		friendDao.delete(toWrapper);
	}
	
}
