package com.asm.zim.server.entry;

import com.asm.zim.common.constants.MessageReadState;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@ToString
@Data
@TableName(value = "message")
public class Message implements Serializable, Cloneable {
	@TableId
	private String id;
	@TableField(value = "person_id")
	private String personId;
	@TableField(value = "from_id")
	private String fromId;
	@TableField(value = "to_id")
	private String toId;
	@TableField
	private String content;
	@TableField(value = "send_time")
	private long sendTime;
	@TableField(value = "message_type")
	private int messageType;
	@TableField(value = "message_category")
	private int messageCategory;
	@TableField(value = "chat_type")
	private int chatType;
	@TableField(value = "terminal_type")
	private int terminalType;
	@TableField
	private String protocol;
	@TableField(value = "read_state")
	private int readState = MessageReadState.UN_READ;
	@TableField(value = "order_number")
	private long orderNumber;
	@TableField(exist = false)
	private MessageFile messageFile;
}
