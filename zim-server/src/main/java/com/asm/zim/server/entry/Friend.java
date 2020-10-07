package com.asm.zim.server.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Data
@TableName("friend")
public class Friend implements Serializable {
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	@TableField(value = "friend_group_id")
	private String friendGroupId;
	@TableField(value = "remark_name")
	private String remarkName;
	/**
	 * 自己的Id
	 */
	@TableField(value = "person_id")
	private String personId;
	/**
	 * 好友 personId
	 */
	@TableField(value = "friend_id")
	private String friendId;
	/**
	 * 好友Person 信息
	 */
	@TableField(exist = false)
	private Person person;
}
