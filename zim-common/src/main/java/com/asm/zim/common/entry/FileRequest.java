package com.asm.zim.common.entry;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description 上传的file
 */
@Data
public class FileRequest implements Serializable, Cloneable {
	private String id;
	private String fileName;
	private String suffix;
	private long size;
	private long createTime;
	private byte[] bytes;
	
	
	public FileRequest() {
	}
	
	public FileRequest(String fileName, byte[] bytes) {
		fileName = fileName == null ? "" : fileName;
		this.fileName = fileName;
		this.bytes = bytes;
		this.size = bytes.length;
		suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		createTime = System.currentTimeMillis();
	}
	
}
