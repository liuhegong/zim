package com.asm.zim.server.config.yaml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author : azhao
 * @description 文件服务器配置
 */
@Component
@ConfigurationProperties(prefix = "im.file-server")
@Data
@Validated
public class FileServerConfig {
	private String url = "http://127.0.0.1:7070";
}
