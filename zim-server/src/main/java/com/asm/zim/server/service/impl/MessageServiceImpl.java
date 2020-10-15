package com.asm.zim.server.service.impl;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.common.constants.ChatType;
import com.asm.zim.common.constants.MessageCategory;
import com.asm.zim.common.constants.MessageReadState;
import com.asm.zim.common.constants.MessageType;
import com.asm.zim.common.entry.FileResponse;
import com.asm.zim.file.client.api.FileManageService;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.common.constants.MyPage;
import com.asm.zim.server.dao.MessageDao;
import com.asm.zim.server.dao.MessageFileDao;
import com.asm.zim.server.entry.Message;
import com.asm.zim.server.entry.MessageFile;
import com.asm.zim.server.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : azhao
 * @description
 */
@Service
public class MessageServiceImpl implements MessageService {
	private Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private MessageFileDao messageFileDao;
	@Autowired
	private FileManageService fileManageService;
	@Override
	public void addMessage(Message message) {
		if (message.getId() == null) {
			message.setId(IdUtil.simpleUUID());
		}
		
		messageDao.insert(message);
	}
	
	@Override
	public IPage<Message> findMessage(int page, Map<String, Object> map) {
		return findMessage(page, Constants.DEFAULT_PAGE_SIZE, map);
	}
	
	
	@Override
	public IPage<Message> findMessage(int page, int pageSize, Map<String, Object> map) {
		if (map == null) {
			map = new HashMap<>();
		}
		return messageDao.selectMessageList(new MyPage<>(page, pageSize), map);
	}
	
	
	@Override
	public IPage<Message> findPrivateMessage(int page, int pageSize, String personId, String fromId, String toId, int readState) {
		return findMessage(page, pageSize, findPrivateMap(personId, fromId, toId, readState));
	}
	
	@Override
	public IPage<Message> loadPrivate(int page, String personId, String fromId, String toId, int readState) {
		IPage<Message> pageResult = findPrivateMessage(page, Constants.DEFAULT_PAGE_SIZE, personId, fromId, toId, readState);
		// TODO: 2020/9/3 message foreach 更新
		if (readState == MessageReadState.UN_READ || readState == MessageReadState.ALL) {
			List<Message> messageList = pageResult.getRecords();
			for (Message message : messageList) {
				UpdateWrapper<Message> wrapper = new UpdateWrapper<>();
				wrapper.eq("id", message.getId());
				message.setReadState(MessageReadState.HAS_READ);
				messageDao.update(message, wrapper);
			}
		}
		return pageResult;
	}
	
	@Override
	public Map<String, Long> findPrivateUnReadCount(String personId) {
		// TODO: 2020/9/3 个人分组查询
		Page<Message> page = new Page<>(1, Integer.MAX_VALUE);
		QueryWrapper<Message> wrapper = new QueryWrapper<>();
		wrapper.eq("person_id", personId);
		wrapper.eq("to_id", personId);
		wrapper.eq("read_state", 0);
		IPage<Message> pageResult = messageDao.selectPage(page, wrapper);
		List<Message> messageList = pageResult.getRecords();
		return messageList.stream().collect(Collectors.groupingBy(Message::getFromId, Collectors.counting()));
	}
	
	@Override
	public void setPrivateHasRead(String personId, String fromId, String toId) {
		Map<String, Object> map = new HashMap<>();
		map.put("readState", MessageReadState.HAS_READ);
		map.put("personId", personId);
		map.put("fromId", fromId);
		map.put("toId", toId);
		messageDao.setHasRead(map);
	}
	
	@Override
	public void removeMessage(String id) {
		// TODO: 2020/9/21 文件删除
		Message message = messageDao.selectById(id);
		if (message.getMessageCategory() == MessageCategory.File) {
			// fileManageService.removeFile();
		}
	}
	
	@Override
	public void saveSend(Message message) {
		if (message.getMessageCategory() == MessageCategory.File) {
			messageFileDao.insert(message.getMessageFile());
		}
		logger.info("开始保存消息");
		message.setPersonId(message.getFromId());
		message.setReadState(MessageReadState.HAS_READ);
		addMessage(message);
	}
	
	@Override
	public void saveReceive(Message message) {
		//接收的是文件
		if (message.getMessageCategory() == MessageCategory.File) {
			//接收文件复制 不共用
			MessageFile messageFile = message.getMessageFile();
			FileResponse fileResponse = fileManageService.copy(messageFile.getId());
			if (fileResponse != null) {
				messageFile.setId(fileResponse.getId());
				messageFile.setUrl(fileResponse.getUrl());
				messageFile.setMessageId(message.getId());
				messageFileDao.insert(messageFile);
			}
		}
		logger.info("开始保存消息");
		message.setReadState(MessageReadState.UN_READ);
		addMessage(message);
	}
	
	
	private Map<String, Object> findPrivateMap(String personId, String fromId, String toId, int readState) {
		Map<String, Object> map = new HashMap<>();
		map.put("personId", personId);
		map.put("messageType", MessageType.Ordinary);
		map.put("chatType", ChatType.privateChat);
		map.put("toId", toId);
		map.put("fromId", fromId);
		if (readState != MessageReadState.ALL) {
			map.put("readState", readState);
		}
		return map;
	}
	
}
