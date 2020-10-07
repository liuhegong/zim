package com.asm.zim.server.controller;

import com.asm.zim.file.client.api.FileManageService;
import com.asm.zim.server.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author : azhao
 * @description
 */
@RestController
@RequestMapping("file")
public class FileController {
	private Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
	private FileManageService fileManageService;
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping("download/{id}")
	public void download(@PathVariable("id") String id, HttpServletResponse response) {
		String token = tokenService.getToken();
		fileManageService.download(id,token,response);
	}
}
