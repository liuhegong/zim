package com.asm.zim.server.core.service;

import com.asm.zim.common.entry.TokenAuth;
import com.asm.zim.server.core.container.LocalChannelGroup;
import com.asm.zim.server.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author : azhao
 * @description 定时检测 检测checkChannelMap
 */
@Service
public class ChannelJobService {
	private Logger logger = LoggerFactory.getLogger(ChannelJobService.class);
	@Autowired
	private LocalChannelGroup socketChannelGroup;
	@Autowired
	private TokenService tokenService;
	
	@Value("#{${im.check-channel-map}}")
	private boolean startCheckChannelMap = false;
	
	/**
	 * 定时检测channel map  每八个小时检测一次
	 */
	@Scheduled(cron = "0 0 0/8 * * ?")
	public void checkChannelMap() {
		if (startCheckChannelMap) {
			logger.info("执行checkChannelMap 开始");
			long start = System.currentTimeMillis();
			Set<String> tokenSet = socketChannelGroup.getChannelMap().keySet();
			for (String token : tokenSet) {
				TokenAuth tokenAuth = tokenService.getTokenAuth(token);
				if (tokenAuth == null) {
					socketChannelGroup.removeChannel(token);
				}
			}
			long end = System.currentTimeMillis();
			logger.info("执行checkChannelMap 结束,用时 {} 毫秒", (end - start));
		}
	}
	
}
