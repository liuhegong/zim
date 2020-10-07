package com.asm.zim.server.service.impl;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.constants.ChatType;
import com.asm.zim.common.constants.MessageType;
import com.asm.zim.common.constants.Result;
import com.asm.zim.common.constants.VerificationState;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.common.constants.MyPage;
import com.asm.zim.server.core.chat.SystemChatService;
import com.asm.zim.server.dao.FriendVerificationDao;
import com.asm.zim.server.entry.Friend;
import com.asm.zim.server.entry.FriendVerification;
import com.asm.zim.server.entry.Person;
import com.asm.zim.server.service.FriendService;
import com.asm.zim.server.service.FriendVerificationService;
import com.asm.zim.server.service.PersonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : azhao
 * @description
 */
@Service
public class FriendVerificationServiceImpl implements FriendVerificationService {
	private Logger logger = LoggerFactory.getLogger(FriendVerificationServiceImpl.class);
	@Autowired
	private FriendVerificationDao friendVerificationDao;
	@Autowired
	private PersonService personService;
	@Autowired
	private SystemChatService systemChatService;
	
	@Autowired
	private FriendService friendService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result<String> send(FriendVerification fv) {
		fv.setId(IdUtil.fastSimpleUUID());
		FriendVerification receive = fv.clone();
		receive.setPersonId(receive.getToId());
		receive.setId(IdUtil.fastSimpleUUID());
		receive.setGroupId(null);
		receive.setRemarkName(null);
		fv.setBindId(receive.getId());
		receive.setBindId(fv.getId());
		friendVerificationDao.insert(fv);
		friendVerificationDao.insert(receive);
		return new Result<String>().success("请求发送成功");
	}
	
	@Override
	public IPage<FriendVerification> load(int page, String personId, String toId) {
		Map<String, Object> map = new HashMap<>();
		map.put("personId", personId);
		if (toId != null) {
			map.put("toId", toId);
		}
		return friendVerificationDao.load(new MyPage<>(page, Constants.DEFAULT_PAGE_SIZE), map);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void agreeFriend(String id, String remarkName, String groupId) {
		FriendVerification fv = friendVerificationDao.selectById(id);
		QueryWrapper<FriendVerification> toWrapper = new QueryWrapper<>();
		toWrapper.eq("id", fv.getBindId());
		//对方
		FriendVerification toFv = friendVerificationDao.selectOne(toWrapper);
		toFv.setAgreeState(VerificationState.Agree);
		friendVerificationDao.updateById(toFv);
		Friend toFriend = new Friend();
		toFriend.setFriendGroupId(toFv.getGroupId());
		toFriend.setFriendId(toFv.getToId());
		toFriend.setPersonId(toFv.getPersonId());
		if (StringUtils.isEmpty(remarkName)) {
			Person person = personService.findPerson(toFv.getPersonId());
			toFriend.setRemarkName(person.getName());
		} else {
			toFriend.setRemarkName(toFv.getRemarkName());
		}
		friendService.add(toFriend);
		logger.info("对方 好友请求同意 {}", toFriend);
		//自己
		fv.setAgreeState(VerificationState.Agree);
		friendVerificationDao.updateById(fv);
		Friend ownFriend = new Friend();
		if (StringUtils.isEmpty(remarkName)) {
			Person person = personService.findPerson(fv.getFromId());
			ownFriend.setRemarkName(person.getName());
		} else {
			ownFriend.setRemarkName(remarkName);
		}
		ownFriend.setFriendGroupId(groupId);
		ownFriend.setPersonId(fv.getPersonId());
		ownFriend.setFriendId(fv.getFromId());
		friendService.add(ownFriend);
		sendNotice(fv.getId(), fv.getFromId());
		logger.info("自己 请求同意 {}", ownFriend);
	}
	
	//发送系统通知
	private void sendNotice(String fromId, String toId) {
		BaseMessage.Message.Builder builder = BaseMessage.Message.newBuilder();
		builder.setId(IdUtil.fastSimpleUUID());
		builder.setFromId(fromId);
		builder.setMessageType(MessageType.System);
		builder.setChatType(ChatType.privateChat);
		builder.setSendTime(System.currentTimeMillis());
		builder.setToId(toId);
		systemChatService.sendFriendRequest(builder.build());
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void refuseFriend(String id, String content) {
		FriendVerification fv = friendVerificationDao.selectById(id);
		QueryWrapper<FriendVerification> toWrapper = new QueryWrapper<>();
		toWrapper.eq("id", fv.getBindId());
		//对方
		FriendVerification toFv = friendVerificationDao.selectOne(toWrapper);
		toFv.setAgreeState(VerificationState.Refuse);
		toFv.setContent(content);
		friendVerificationDao.updateById(toFv);
		
		fv.setContent(content);
		fv.setAgreeState(VerificationState.Refuse);
		friendVerificationDao.updateById(fv);
		sendNotice(fv.getId(), fv.getFromId());
		
		logger.info("好友拒绝 toFv{} fv{}",toFv,fv);
	}
}
