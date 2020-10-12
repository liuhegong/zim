package com.asm.zim.file.server.common;

import cn.hutool.core.io.FileUtil;
import com.asm.zim.file.server.config.ImConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : azhao
 */
@Configuration
public class Constants {
	@Autowired
	private ImConfig imConfig;
	
	@PostConstruct
	public void init() {
		BASE_FILE_PATH = imConfig.getFilePath();
		if (!FileUtil.exist(BASE_FILE_PATH)) {
			FileUtil.mkdir(BASE_FILE_PATH);
		}
	}
	
	public static String BASE_FILE_PATH;
}
