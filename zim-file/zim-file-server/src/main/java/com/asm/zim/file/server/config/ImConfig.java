package com.asm.zim.file.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author : azhao
 */
@Component
@Data
@Validated
@ConfigurationProperties(prefix = "im")
public class ImConfig {
	private String filePath;
}
