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
public class MessageFile implements Serializable, Cloneable {
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	@TableField(value = "message_id")
	private String messageId;
	@TableField(value = "file_name")
	private String fileName;
	@TableField
	private long size;
	@TableField
	private String suffix;
	@TableField
	private String url;
	@TableField(value = "create_time")
	private long createTime;
}
