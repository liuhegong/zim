package com.asm.zim.server.service;

import com.asm.zim.server.entry.Message;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
 * @author : azhao
 * @description
 */
public interface MessageService {
	/**
	 * 不添加附件
	 *
	 * @param message
	 */
	void addMessage(Message message);
	
	/**
	 * @param page 默认 DEFAULT_PAGE_SIZE
	 * @param map  可以为 null
	 * @return
	 */
	IPage<Message> findMessage(int page, Map<String, Object> map);
	
	/**
	 * @param page
	 * @param map  personId,messageType,chatType,terminalType
	 * @return
	 */
	IPage<Message> findMessage(int page, int pageSize, Map<String, Object> map);
	
	/**
	 * 查看私聊信息
	 *
	 * @param personId
	 * @param toId     如果为空 则查询所有
	 * @return
	 */
	IPage<Message> findPrivateMessage(int page, int pageSize, String personId, String fromId, String toId, int readState);
	
	/**
	 * @param personId 某个人的message
	 * @param toId     来自某个人发送的message
	 * @return
	 */
	IPage<Message> loadPrivate(int page, String personId, String fromId, String toId, int readState);
	
	/**
	 * 查询某些好友未读消息个数
	 *
	 * @param personId
	 * @return
	 */
	Map<String, Long> findPrivateUnReadCount(String personId);
	
	/**
	 * 设置已读
	 *
	 * @param personId
	 * @param fromId
	 * @param toId
	 */
	void setPrivateHasRead(String personId, String fromId, String toId);
	
	
	void removeMessage(String id);
	
	void saveSend(Message message);
	
	void saveReceive(Message message);
}
