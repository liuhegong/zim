package com.asm.zim.common.entry;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : azhao
 * @description 添加好友的消息
 */
@Data
public class FriendRequestMessage implements Serializable {
	private String groupId;
	private String remarkName;
	private String content;
}
