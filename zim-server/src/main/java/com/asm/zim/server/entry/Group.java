package com.asm.zim.server.entry;

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
@TableName(value = "group")
public class Group implements Serializable {
	@TableId
	private String id;
	@TableField
	private String name;
	@TableField(value = "create_time")
	private long createTime;
	@TableField
	private String icon;
	@TableField
	private String intro;
	@TableField(exist = false)
	private List<Person> personList;
}
