package com.asm.zim.server.entry;

import com.asm.zim.common.constants.MessageReadState;
import com.asm.zim.common.constants.VerificationState;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : azhao
 * @description 好友验证
 */
@Data
@ToString
public class FriendVerification implements Serializable, Cloneable {
	@TableId
	private String id;
	@TableField(value = "person_id")
	private String personId;
	@TableField(value = "from_id")
	private String fromId;
	@TableField(exist = false)
	private Person fromPerson;
	@TableField(exist = false)
	private Person toPerson;
	@TableField(value = "to_id")
	private String toId;
	@TableField
	private String content;
	/**
	 * 相应返回年内容这里只是相应的信息把请求的信息覆盖掉
	 */
	@TableField(exist = false)
	private String responseInfo;
	@TableField(value = "group_id")
	private String groupId;
	@TableField(value = "remark_name")
	private String remarkName;
	@TableField(value = "read_state")
	private int readState = MessageReadState.HAS_READ;
	@TableField(value = "agree_state")
	private int agreeState = VerificationState.Wait;
	@TableField(value = "send_time")
	private long sendTime;
	/**
	 * 响应的 id 与请求id 相互绑定
	 */
	@TableField(value = "bind_id")
	private String bindId;
	
	@Override
	public FriendVerification clone() {
		FriendVerification f = null;
		try {
			f = (FriendVerification) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return f;
	}
}
