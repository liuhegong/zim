package com.asm.zim.server.common.aop;

import cn.hutool.core.util.IdUtil;
import com.asm.zim.server.annotation.MyLog;
import com.asm.zim.server.common.constants.Constants;
import com.asm.zim.server.config.yaml.WebsocketConfig;
import com.asm.zim.server.controller.LoginController;
import com.asm.zim.server.dao.LogDao;
import com.asm.zim.server.entry.Log;
import com.asm.zim.server.service.TokenService;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
/*
 *
 * @author : azhao
 * @description
 */


@Component
@Aspect
public class LogAdvices {
	private Logger logger = LoggerFactory.getLogger(LogAdvices.class);
	@Setter
	@Getter
	private boolean aroundLog = false;
	@Autowired
	private LogDao logDao;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private WebsocketConfig websocketConfig;
	@Pointcut("execution(* com.asm.zim.server.controller..*(..))")
	public void pointcut() {
	}
	
	@Before("@annotation(com.asm.zim.server.annotation.MyLog)")
	public void before(JoinPoint jp) {
		// TODO: 2020/ 9/25 支持tcp
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		if (method.isAnnotationPresent(MyLog.class)) {
			MyLog myLog = method.getAnnotation(MyLog.class);
			Log log = new Log();
			log.setId(IdUtil.fastSimpleUUID());
			log.setArg(Arrays.toString(jp.getArgs()));
			log.setRecordTime(System.currentTimeMillis());
			log.setServerIp(Constants.LOCALHOST);
			log.setHttpPort(Constants.HTTP_PORT);
			log.setWebSocketPort(websocketConfig.getPort());
			log.setMethod(method.getName());
			log.setDescribe(myLog.describe());
			String personId = tokenService.getPersonId();
			log.setPersonId(personId);
			logDao.insert(log);
		}
	}
	
	
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		// TODO: 2020/ 9/25 支持tcp
		Object result = pjp.proceed();
		String clazzName = pjp.getTarget().getClass().getName();
		if (aroundLog && !LogAdvices.class.getName().equals(clazzName)) {
			MethodSignature signature = (MethodSignature) pjp.getSignature();
			Method method = signature.getMethod();
			Log log = new Log();
			log.setId(IdUtil.fastSimpleUUID());
			Object[] args = pjp.getArgs();
			if (args != null) {
				log.setArg(Arrays.toString(args));
				if (LoginController.class.getName().equals(clazzName)) {
					log.setArg("***");
				}
			}
			
			log.setRecordTime(System.currentTimeMillis());
			log.setServerIp(Constants.LOCALHOST);
			log.setHttpPort(Constants.HTTP_PORT);
			log.setWebSocketPort(websocketConfig.getPort());
			if (result != null) {
				log.setResult(result.toString());
			}
			log.setMethod(method.getName());
			String personId = tokenService.getPersonId();
			log.setPersonId(personId);
			logDao.insert(log);
		}
		return result;
	}
}
