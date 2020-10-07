package com.asm.zim.server;

import com.asm.zim.common.entry.NetMessage;
import com.asm.zim.common.proto.BaseMessage;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.core.service.DataProtocolService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;

/**
 * @author : azhao
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTest {
	private Logger logger = LoggerFactory.getLogger(RedisTemplateTest.class);
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	@Autowired
	private DataProtocolService dataProtocolService;
	@Test
	public void test1() {
		BaseMessage.Message.Builder builder = BaseMessage.Message.newBuilder();
		builder.setContent("xxx");
		NetMessage netMessage = dataProtocolService.coverProtoMessageToNetMessage(builder.build());
		redisTemplate.convertAndSend(Constants.REDIS_MESSAGE_RECEIVE_TOPIC, netMessage);
	}
}
