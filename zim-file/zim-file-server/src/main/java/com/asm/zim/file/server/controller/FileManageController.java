package com.asm.zim.file.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.asm.zim.common.constants.Result;
import com.asm.zim.common.entry.FileResponse;
import com.asm.zim.file.server.common.Constants;
import com.asm.zim.file.server.entry.FilePO;
import com.asm.zim.file.server.service.BeanConvertService;
import com.asm.zim.file.server.service.FileManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : azhao
 * @description
 */
@RestController
@RequestMapping("file")
public class FileManageController {
	private Logger logger = LoggerFactory.getLogger(FileManageController.class);
	@Autowired
	private FileManageService fileManageService;
	@Autowired
	private BeanConvertService beanConvertService;
	
	@RequestMapping("download/{id}")
	public void download(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
		FilePO filePO = fileManageService.getFilePo(id);
		if (filePO != null && filePO.getPath() != null) {
			OutputStream os = null;
			FileInputStream fs;
			BufferedInputStream bis = null;
			response.setCharacterEncoding("UTF-8");
			try {
				response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filePO.getFileName(), "UTF-8"));
				response.setContentType("application/octet-stream");
				os = response.getOutputStream();
				File file = new File(Constants.BASE_FILE_PATH + filePO.getPath());
				fs = new FileInputStream(file);
				bis = new BufferedInputStream(fs);
				byte[] buffer = new byte[1024];
				os = response.getOutputStream();
				int len;
				while ((len = bis.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
				os.flush();
			} catch (
					IOException e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					bis.close();
				}
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			response.setContentType("application/json;charset=utf-8");
			PrintWriter pw = response.getWriter();
			Result<String> result = new Result<String>().failure("文件不存在");
			pw.write(JSONObject.toJSONString(result));
			pw.close();
		}
	}
	
	@RequestMapping("remove/{id}")
	public Result<String> remove(@PathVariable(name = "id") String id) {
		fileManageService.remove(id);
		return new Result<String>().success();
	}
	
	@RequestMapping("multiUpload")
	public Result<List<FileResponse>> upload(MultipartFile[] file) throws IOException {
		List<FilePO> filePOList = fileManageService.upload(file);
		if (filePOList != null) {
			List<FileResponse> urlList = new LinkedList<>();
			for (FilePO filePO : filePOList) {
				FileResponse fileResponse = beanConvertService.filePoToFileResponse(filePO);
				urlList.add(fileResponse);
			}
			return new Result<List<FileResponse>>().success(urlList);
		}
		return new Result<List<FileResponse>>().failure();
	}
	
	@RequestMapping("upload")
	public Result<FileResponse> upload(MultipartFile file) throws IOException {
		FilePO filePO = fileManageService.upload(file);
		if (filePO != null) {
			FileResponse fileResponse = beanConvertService.filePoToFileResponse(filePO);
			return new Result<FileResponse>().success(fileResponse);
		}
		return new Result<FileResponse>().failure();
	}
	
	@RequestMapping("copy")
	public Result<FileResponse> copy(String id) {
		FilePO filePO = fileManageService.copy(id);
		if (filePO != null) {
			FileResponse fileResponse = beanConvertService.filePoToFileResponse(filePO);
			return new Result<FileResponse>().success(fileResponse);
		}
		return new Result<FileResponse>().failure();
	}
	
	@RequestMapping("getInfo")
	public Result<FileResponse> getFileInfo(String id) {
		FilePO filePO = fileManageService.getFilePo(id);
		if (filePO != null) {
			FileResponse fileResponse = beanConvertService.filePoToFileResponse(filePO);
			return new Result<FileResponse>().success(fileResponse);
		}
		return new Result<FileResponse>().failure();
	}
}
