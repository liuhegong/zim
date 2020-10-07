package com.asm.zim.server.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Data
public class Person implements Serializable {
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	@TableField(value = "person_name")
	private String name;
	@TableField
	private String intro;
	@TableField
	public String header;
	
}
