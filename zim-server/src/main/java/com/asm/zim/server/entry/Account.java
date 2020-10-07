package com.asm.zim.server.entry;

import com.asm.zim.server.common.constants.AccountState;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Data
@TableName(value = "account")
public class Account implements Serializable {
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	@TableField(insertStrategy = FieldStrategy.NOT_NULL)
	private String username;
	@TableField(insertStrategy = FieldStrategy.NOT_NULL)
	private String pwd;
	@TableField
	private int state = AccountState.NORMAL;
	
	public Account(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
	}
	
	public Account() {
	}
}
