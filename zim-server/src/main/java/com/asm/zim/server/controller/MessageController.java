package com.asm.zim.server.controller;

import com.asm.zim.common.constants.MessageReadState;
import com.asm.zim.common.constants.Result;
import com.asm.zim.common.entry.FileRequest;
import com.asm.zim.common.entry.FileResponse;
import com.asm.zim.file.client.api.FileManageService;
import com.asm.zim.server.entry.Message;
import com.asm.zim.server.entry.MessageFile;
import com.asm.zim.server.service.MessageService;
import com.asm.zim.server.service.TokenService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author : azhao
 * @description
 */
@RestController
@RequestMapping("message")
public class MessageController {
	private Logger logger = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private MessageService messageService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private FileManageService fileManageService;
	
	/**
	 * 加载某个人 未读的
	 *
	 * @param toId
	 * @return
	 */
	@RequestMapping("/loadPrivate")
	public Result<IPage<Message>> loadPrivate(String toId, int page) {
		String personId = tokenService.getPersonId();
		IPage<Message> messageIPage = messageService.loadPrivate(page, personId, toId, personId, MessageReadState.ALL);
		return new Result<IPage<Message>>().success(messageIPage);
	}
	
	/**
	 * 查看所有未读的个数
	 *
	 * @return
	 */
	@RequestMapping("/findPrivateUnReadCount")
	public Result<Map<String, Long>> findPrivateUnReadCount() {
		String personId = tokenService.getPersonId();
		Map<String, Long> result = messageService.findPrivateUnReadCount(personId);
		return new Result<Map<String, Long>>().success(result);
	}
	
	/**
	 * 对某人设置已读
	 *
	 * @param toId
	 * @return
	 */
	@RequestMapping("/setPrivateHasRead")
	public Result<String> setPrivateHasRead(String toId, String fromId) {
		String personId = tokenService.getPersonId();
		messageService.setPrivateHasRead(personId, fromId, toId);
		return new Result<String>().success();
	}
	
	@RequestMapping("/uploadMessageFile")
	public Result<MessageFile> uploadMessageFile(MultipartFile file) {
		String token = tokenService.getToken();
		try {
			FileRequest fileEntry = new FileRequest(file.getOriginalFilename(), file.getBytes());
			FileResponse fileResponse = fileManageService.upload(fileEntry);
			if (fileResponse == null) {
				return new Result<MessageFile>().failure();
			}
			MessageFile messageFile = new MessageFile();
			messageFile.setId(fileResponse.getId());
			messageFile.setUrl("/file/download/" + fileResponse.getId());
			messageFile.setSize(fileResponse.getSize());
			messageFile.setFileName(file.getOriginalFilename());
			messageFile.setCreateTime(fileResponse.getCreateTime());
			messageFile.setSuffix(fileResponse.getSuffix());
			return new Result<MessageFile>().success(messageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Result<MessageFile>().failure();
	}
}
