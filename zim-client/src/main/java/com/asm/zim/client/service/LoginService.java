package com.asm.zim.client.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.asm.zim.client.common.Common;
import com.asm.zim.client.common.Constants;
import com.asm.zim.common.constants.*;
import com.asm.zim.common.proto.BaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.asm.zim.client.common.Common.*;

/**
 * @author : azhao
 * @description
 */
public class LoginService {
	private Logger logger = LoggerFactory.getLogger(LoginService.class);
	
	private String baseUrl = Constants.HTTP_URL;
	
	public void writeMessage() {
		String token = "80b626fc4d634c5a857e7d0163d05226";//zhangsan
		String personId = "eed71d2a6249420ca486ab812f48ed9e";
		String toId = "e1df181eeb8b4e3083780f4a5f1fc17f";//lisi
		while (true) {
			Scanner scanner = new Scanner(System.in);
			String nextLine = scanner.nextLine();
			if (nextLine.equals("exit")) {
				return;
			}
			BaseMessage.Message.Builder builder = BaseMessage.Message.newBuilder();
			builder.setToId(toId);
			builder.setContent(nextLine);
			builder.setId(IdUtil.fastSimpleUUID());
			builder.setSendTime(System.currentTimeMillis());
			builder.setProtocol(NetProtocol.TCP);
			builder.setToken(token);
			builder.setFromId(personId);
			builder.setTerminalType(TerminalType.Android.getValue());
			builder.setMessageType(MessageType.Ordinary);
			builder.setChatType(ChatType.privateChat);
			builder.setMessageCategory(MessageCategory.Text);
			BaseMessage.Message newMessage = builder.build();
			logger.info(newMessage.toString());
			channel.writeAndFlush(newMessage);
		}
	}
	
	public void tcpLogin() {
		BaseMessage.Message.Builder builder = BaseMessage.Message.newBuilder();
		builder.setId(IdUtil.fastSimpleUUID());
		builder.setSendTime(System.currentTimeMillis());
		builder.setProtocol(NetProtocol.TCP);
		builder.setToken(Common.token);
		builder.setFromId(personId);
		builder.setTerminalType(TerminalType.Android.getValue());
		builder.setMessageType(MessageType.System);
		builder.setCode(MessageCode.LoginRequest);
		channel.writeAndFlush(builder.build());
	}
	
	public boolean login(String username, String pwd) {
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("pwd", pwd);
		String resultJson = HttpUtil.post(baseUrl + "/login", map);
		Result result = JSONObject.parseObject(resultJson, Result.class);
		if (result.getCode() == ResultCode.Success.getCode()) {
			JSONObject jsonObject = (JSONObject) result.getData();
			expireTime = Integer.parseInt(jsonObject.get("expireTime") == null ? "3000" : String.valueOf(jsonObject.get("expireTime")));
			token = String.valueOf(jsonObject.get("token"));
			personId = String.valueOf(jsonObject.get("personId"));
			return true;
		}
		return false;
	}

}
