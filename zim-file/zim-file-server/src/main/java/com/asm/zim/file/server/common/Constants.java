package com.asm.zim.file.server.common;

import cn.hutool.core.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : azhao
 * @description
 */
@Configuration
@Component
public class Constants {
	@Value("${im.file-path}")
	private String filePath;
	
	@PostConstruct
	public void init() {
		BASE_FILE_PATH = filePath;
		if (!FileUtil.exist(BASE_FILE_PATH)) {
			FileUtil.mkdir(BASE_FILE_PATH);
		}
	}
	
	public static String BASE_FILE_PATH;
}
