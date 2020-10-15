package com.asm.zim.file.client.api.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asm.zim.common.constants.Result;
import com.asm.zim.common.constants.ResultCode;
import com.asm.zim.common.entry.FileRequest;
import com.asm.zim.common.entry.FileResponse;
import com.asm.zim.file.client.api.FileManageService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : azhao
 * @description
 */
@Service
public class FileManageServiceImpl implements FileManageService {
	
	private Logger logger = LoggerFactory.getLogger(FileManageServiceImpl.class);
	
	private String baseUrl;
	/**
	 * 上传连接超时时间
	 */
	private int timeOut = 5000;
	
	public FileManageServiceImpl() {
	}
	
	public FileManageServiceImpl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public FileManageServiceImpl(String baseUrl, int timeOut) {
		this.baseUrl = baseUrl;
		this.timeOut = timeOut;
	}
	
	@Override
	public void removeFile(String id) {
		Map<String, Object> map = new HashMap<>();
		String result = HttpUtil.post(baseUrl + "/file/remove/" + id, map);
		logger.info(result);
	}
	
	private CloseableHttpClient client;
	private RequestConfig requestConfig;
	
	@PostConstruct
	public void initHttpClient() {
		client = HttpClients.createDefault();
		requestConfig = RequestConfig.custom()
				.setConnectTimeout(timeOut).build();
	}
	
	@PreDestroy
	public void destroy() {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param url  真实url
	 * @param list
	 * @return 上传失败返回 null
	 */
	public List<FileResponse> upload(String url, List<FileRequest> list) {
		if (list == null) {
			return null;
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			builder.setCharset(Charset.forName("UTF-8"));
			for (FileRequest ufe : list) {
				builder.addBinaryBody("file", ufe.getBytes(),
						ContentType.MULTIPART_FORM_DATA, ufe.getFileName());
			}
			httpPost.setEntity(builder.build());
			HttpResponse response = client.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) { // 将响应内容转换为字符串
				String json = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
				Result result = JSON.parseObject(json, Result.class);
				JSONArray jsonArray = (JSONArray) result.getData();
				if (jsonArray == null) {
					return null;
				} else {
					return JSONObject.parseArray(jsonArray.toJSONString(), FileResponse.class);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param url       真实url
	 * @param fileEntry
	 * @return
	 */
	public FileResponse upload(String url, FileRequest fileEntry) {
		if (fileEntry == null) {
			return null;
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			builder.setCharset(Charset.forName("UTF-8"));
			builder.addBinaryBody("file", fileEntry.getBytes(),
					ContentType.MULTIPART_FORM_DATA, fileEntry.getFileName());
			httpPost.setEntity(builder.build());
			HttpResponse response = client.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) { // 将响应内容转换为字符串
				String json = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
				Result result = JSON.parseObject(json, Result.class);
				JSONObject jsonObject = (JSONObject) result.getData();
				if (jsonObject == null) {
					return null;
				} else {
					return JSONObject.parseObject(jsonObject.toJSONString(), FileResponse.class);
				}
			}
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			logger.info("连接超时");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public FileResponse upload(FileRequest uploadFileEntry) {
		return upload(baseUrl + "/file/upload", uploadFileEntry);
	}
	
	public List<FileResponse> upload(List<FileRequest> list) {
		return upload(baseUrl + "/file/multiUpload", list);
	}
	
	@Override
	public void download(String id, HttpServletResponse response) {
		String url = baseUrl + "/file/download/" + id;
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			FileResponse fileResponse = getInfo(id);
			if (fileResponse == null) {
				Result<String> result = new Result<String>().failure();
				os.write(JSON.toJSONString(result).getBytes());
				os.flush();
				os.close();
				return;
			}
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileResponse.getFileName(), "UTF-8"));
			response.addHeader("Content-Length", "" + fileResponse.getSize());
			long size = HttpUtil.download(url, os, true);
			logger.info("下载 大小 {} B", size);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public FileResponse copy(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		Result<?> result = post("/file/copy", map);
		return parseFileResponse(result);
	}
	
	@Override
	public FileResponse getInfo(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		Result<?> result = post("/file/getInfo", map);
		return parseFileResponse(result);
	}
	
	private FileResponse parseFileResponse(Result<?> result) {
		if (result.getCode() == ResultCode.Success.getCode()) {
			JSONObject jsonObject = (JSONObject) result.getData();
			if (jsonObject == null) {
				return null;
			}
			return JSONObject.parseObject(jsonObject.toJSONString(), FileResponse.class);
		}
		return null;
	}
	
	@Override
	public Result<?> post(String url, Map<String, Object> map) {
		if (map == null) {
			map = new HashMap<>();
		}
		String json = HttpUtil.post(baseUrl + url, map);
		return JSON.parseObject(json, Result.class);
	}
}
