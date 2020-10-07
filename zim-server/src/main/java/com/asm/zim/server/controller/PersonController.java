package com.asm.zim.server.controller;

import com.asm.zim.common.constants.Result;
import com.asm.zim.common.entry.FileRequest;
import com.asm.zim.common.entry.FileResponse;
import com.asm.zim.common.utils.FileUtil;
import com.asm.zim.file.client.api.FileManageService;
import com.asm.zim.server.core.container.OnlineManager;
import com.asm.zim.server.entry.Person;
import com.asm.zim.server.service.PersonService;
import com.asm.zim.server.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author : azhao
 * @description
 */

@RestController
@RequestMapping("/person")
public class PersonController {
	private Logger logger = LoggerFactory.getLogger(PersonController.class);
	
	@Autowired
	private PersonService personService;
	@Autowired
	private OnlineManager onlineManager;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private FileManageService fileManageService;
	
	@RequestMapping("findPersonInfo")
	public Result<Person> findPersonInfo(String id) {
		Person person = personService.findPerson(id);
		return new Result<Person>().success(person);
	}
	
	@RequestMapping("findPersonByUsername")
	public Result<Person> findPersonByUsername(String username) {
		Person person = personService.findPersonByUsername(username);
		return new Result<Person>().success(person);
	}
	
	@RequestMapping("findPersonalInfo")
	public Result<Person> findPersonalInfo() {
		String personId = tokenService.getPersonId();
		if (personId != null) {
			Person person = personService.findPerson(personId);
			return new Result<Person>().success(person);
		}
		return new Result<Person>().failure();
	}
	
	@RequestMapping("getOnlinePersonIds")
	public Result<List<String>> getOnlinePersonIds(String groupId) {
		String personId = tokenService.getPersonId();
		List<String> ids = onlineManager.getOnlinePersonIds(personId, groupId);
		return new Result<List<String>>().success(ids);
	}
	
	/**
	 * 修改头像
	 *
	 * @return
	 */
	@RequestMapping("changeHeader")
	public Result<String> changeHeader(@RequestParam(value = "file") MultipartFile file) throws IOException {
		if (file == null) {
			return new Result<String>().failure("文件为空");
		}
		String token = tokenService.getToken();
		String personId = tokenService.getPersonId();
		Person person = personService.findPerson(personId);
		String oldHeader = person.getHeader();
		FileRequest fileEntry = new FileRequest(file.getOriginalFilename(), file.getBytes());
		FileResponse fileResponse = fileManageService.upload(fileEntry, token);
		if (fileResponse != null) {
			person.setHeader(fileResponse.getUrl());
			personService.updatePersonById(person);
			String fileId = FileUtil.getIdByUrl(oldHeader);
			fileManageService.removeFile(fileId, token);
			return new Result<String>().success(person.getHeader());
		} else {
			return new Result<String>().failure();
		}
	}
	
	@RequestMapping("changeInfo")
	public Result<String> changeInfo(@RequestBody Person person) {
		String personId = tokenService.getPersonId();
		Person oldPerson = personService.findPerson(personId);
		person.setId(oldPerson.getId());
		person.setHeader(oldPerson.getHeader());
		personService.updatePersonById(person);
		return new Result<String>().success();
	}
	
}
