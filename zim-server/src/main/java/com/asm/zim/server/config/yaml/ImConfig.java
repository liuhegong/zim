package com.asm.zim.server.config.yaml;

import com.asm.zim.server.common.constants.RouteWay;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * @author : azhao
 * @description 配置文件
 */
@Component
@ConfigurationProperties(prefix = "im")
@Data
@Validated
public class ImConfig {
	//#路由方式 选择redis 和netty 两种方式
	@NotEmpty
	private String routeWay = RouteWay.NETTY;
	//#子网掩码 确定网络相互通信的网段，选择指定网卡通信
	@NotEmpty
	private String mask;
	private boolean aroundLog = false;
	//是否开启自动 检查过期channelMap
	private boolean checkChannelMap = true;
	private boolean tokenEncrypt = false;
	//token 过期时间 单位为秒 , 2/3 时间 刷新token
	private int sessionExpireTime = 3000;
}
