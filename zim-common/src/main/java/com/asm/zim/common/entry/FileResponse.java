package com.asm.zim.common.entry;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description 上传之后返回的file
 */
@Data
public class FileResponse implements Serializable, Cloneable {
	private String id;
	private String fileName;
	private String suffix;
	private long size;
	private long createTime;
	private String url;
	
	
}
