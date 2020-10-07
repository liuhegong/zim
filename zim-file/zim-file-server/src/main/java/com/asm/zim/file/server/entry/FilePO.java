package com.asm.zim.file.server.entry;

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
@TableName(value = "file")
public class FilePO implements Serializable, Cloneable {
	@TableId
	private String id;
	@TableField(value = "file_name")
	private String fileName;
	@TableField(value = "suffix")
	private String suffix;
	@TableField(value = "size")
	private long size;
	@TableField(value = "path")
	private String path;
	@TableField(value = "create_time")
	private long createTime;
	@TableField(value = "modify_time")
	private long modifyTime;
	@TableField(value = "person_id")
	private String personId;
	@TableField(value = "module")
	private String module;
	
	@Override
	public FilePO clone() {
		try {
			return (FilePO) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
