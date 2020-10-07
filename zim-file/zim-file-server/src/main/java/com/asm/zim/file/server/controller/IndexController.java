package com.asm.zim.file.server.controller;

import com.asm.zim.common.constants.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : azhao
 * @description
 */
@RestController
public class IndexController {
	private Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@RequestMapping("/index")
	public Result<String> index() {
		logger.info("index");
		return new Result<String>().success();
	}
}
