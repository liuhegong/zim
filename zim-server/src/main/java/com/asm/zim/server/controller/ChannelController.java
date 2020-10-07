package com.asm.zim.server.controller;

import com.asm.zim.common.constants.Result;
import com.asm.zim.server.core.container.ChannelGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : azhao
 * @description
 */
@RestController
@RequestMapping("channel")
public class ChannelController {
	@Autowired
	private ChannelGroup channelGroup;
	
	@RequestMapping("remove")
	public Result<String> remove(String token) {
		channelGroup.removeChannel(token);
		return new Result<String>().success();
	}
	
	@RequestMapping("getChannelTokens")
	public Result<Map<String, Object>> getChannelTokens(String ip) {
		Map<String, Object> tokens = channelGroup.getChannelTokens(ip);
		return new Result<Map<String, Object>>().success(tokens);
	}
}
