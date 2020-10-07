package com.asm.zim.server.service;

import com.asm.zim.common.constants.Result;
import com.asm.zim.server.entry.FriendVerification;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author : azhao
 * @description
 */
public interface FriendVerificationService {
	Result<String> send(FriendVerification friendVerification);
	
	IPage<FriendVerification> load(int page, String personId, String toId);
	
	void agreeFriend(String id, String remarkName, String groupId);
	
	void refuseFriend(String id, String content);
}
