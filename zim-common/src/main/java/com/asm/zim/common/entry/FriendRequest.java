package com.asm.zim.common.entry;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Data
public class FriendRequest implements Serializable {
	//备注
	private String remarkName;
	//组Id
	private String groupId;
	//验证信息
	private String verification;
}
