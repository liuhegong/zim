package com.asm.zim.common.entry;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Data
public class NetMessageFile implements Serializable, Cloneable {
	private String id;
	private String messageId;
	private String fileName;
	private long size;
	private String suffix;
	private String url;
	private long createTime;
}
