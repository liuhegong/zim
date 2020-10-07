package com.asm.zim.common.entry;

import com.asm.zim.common.constants.MessageCode;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : azhao
 * @description 长连接系统消息
 */
@Data
public class SystemMessage<T> implements Serializable {
	private int code = MessageCode.ReplySuccess;
	private String message = "应答成功";
	private T data;
}
