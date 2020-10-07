package com.asm.zim.client.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.asm.zim.client.common.Constants;
import com.asm.zim.client.entry.Person;
import com.asm.zim.common.constants.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : azhao
 * @description
 */
public class PersonService {
	private Logger logger = LoggerFactory.getLogger(PersonService.class);
	
	public Result<Person> findPersonInfo(String id) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		HttpCookie httpCookie = new HttpCookie("token","3fe52065ec8d47a1981fab024de0c3df");
		String resultJson = HttpRequest.post(Constants.HTTP_URL + "/person/findPersonInfo")
				.form(paramMap)//表单内容
				.cookie(httpCookie)
				.timeout(20000)//超时，毫秒
				.execute().body();
		Result result = JSONObject.parseObject(resultJson, Result.class);
		logger.info(result.toString());
		return null;
	}
	
	public static void main(String[] args) {
		PersonService personService = new PersonService();
		personService.findPersonInfo("514cc43ba09a473e9bd1ea31ebedc004");
	}
}
