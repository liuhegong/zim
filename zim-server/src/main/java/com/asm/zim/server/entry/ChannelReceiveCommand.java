package com.asm.zim.server.entry;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@Data
public class ChannelReceiveCommand implements Serializable {
	/**
	 *
	 */
	private String cmd;
	private Object data;
	
	public ChannelReceiveCommand(String cmd, Object data) {
		this.cmd = cmd;
		this.data = data;
	}
	
	public ChannelReceiveCommand() {
	}
}
