package com.asm.zim.server.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : azhao
 * @description
 */
@Data
@TableName(value = "friend_group")
public class FriendGroup implements Serializable {
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	@TableField(value = "group_name")
	private String name;
	@TableField(value = "person_id")
	private String personId;
	@TableField(exist = false)
	private List<Friend> friendList;
}
