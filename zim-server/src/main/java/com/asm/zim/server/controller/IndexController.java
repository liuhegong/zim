package com.asm.zim.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : azhao
 * @description
 */
@Controller
public class IndexController {
	private Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@RequestMapping(value = "index")
	public String index() {
		return "";
	}
}
