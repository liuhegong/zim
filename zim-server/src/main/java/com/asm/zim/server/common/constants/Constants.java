package com.asm.zim.server.common.constants;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.asm.zim.common.utils.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : azhao
 * @description 常量
 */
@Configuration
public class Constants {
	private static Logger logger = LoggerFactory.getLogger(Constants.class);
	public static final String SESSION_ID = "token";
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static int SESSION_EXPIRE_TIME = 1800;
	public static String LOCALHOST = "127.0.0.1";
	public static int HTTP_PORT = 8081;
	public static int WEBSOCKET_PORT = 8888;
	public static String MASK = "127.0.0.1/16";
	/**
	 * 当前设备唯一标识
	 */
	public static String EQUIPMENT_ID = IdUtil.fastSimpleUUID();
	
	
	/**
	 * redis 消息
	 */
	public static final String REDIS_MESSAGE_RECEIVE_TOPIC = "redis_message_receive_topic";
	/**
	 * 消息通过personId 发送主题
	 */
	public static final String REDIS_MESSAGE_SEND_TOPIC = "redis_message_send_topic";
	/**
	 * token 来路由发送订阅发布
	 */
	public static final String REDIS_MESSAGE_SEND_BY_TOKEN_TOPIC = "redis_message_send_by_token_topic";
	public static final String REDIS_CHANNEL_TOPIC = "redis_channel_topic";
	public static byte[] tokenEncryptKey = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
	
	public static String FILE_SERVER_URL = "http://127.0.0.1:7070";
	
	public static final String CURRENT_IP_TOKEN_PREFIX = "current_ip_token_prefix#";
	
	@Value("${im.session-expire-time}")
	public void setExpireTime(String expireTime) {
		if (expireTime == null) {
			return;
		}
		Constants.SESSION_EXPIRE_TIME = Integer.parseInt(expireTime);
	}
	
	@Value("${im.file-server.url}")
	public void getFileServerUrl(String url) {
		Constants.FILE_SERVER_URL = url;
	}
	
	@Value("${im.mask}")
	public void getMask(String mask) {
		Constants.MASK = mask;
	}
	
	
	@Value("${server.port}")
	public void setHttpPort(String port) {
		port = port == null ? "8080" : port;
		Constants.HTTP_PORT = Integer.parseInt(port);
	}
	
	@Value("${im.websocket.port}")
	public void setWebsocketPort(String port) {
		port = port == null ? "8888" : port;
		Constants.WEBSOCKET_PORT = Integer.parseInt(port);
	}
	
	@PostConstruct
	public void init() {
		Constants.LOCALHOST = NetUtil.getLocalIp(Constants.MASK);
		logger.info("本机通信IP为 {}" ,Constants.LOCALHOST);
		logger.info("设备号为 {} ",Constants.EQUIPMENT_ID);
	}
}
