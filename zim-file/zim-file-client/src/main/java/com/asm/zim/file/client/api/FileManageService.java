package com.asm.zim.file.client.api;

import com.asm.zim.common.constants.Result;
import com.asm.zim.common.entry.FileRequest;
import com.asm.zim.common.entry.FileResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author azhao
 * @description
 */
public interface FileManageService {
	void removeFile(String id, String token);
	
	/**
	 * @param url   真实url
	 * @param list
	 * @param token
	 * @return
	 */
	List<FileResponse> upload(String url, List<FileRequest> list, String token);
	
	/**
	 * @param url       真实url
	 * @param fileEntry
	 * @param token
	 * @return url
	 */
	FileResponse upload(String url, FileRequest fileEntry, String token);
	
	/**
	 * @param uploadFileEntry
	 * @param token
	 * @return 返回 url
	 */
	FileResponse upload(FileRequest uploadFileEntry, String token);
	
	
	List<FileResponse> upload(List<FileRequest> list, String token);
	
	void download(String id, String token, HttpServletResponse response);
	
	/**
	 * 根据 文件 id 复制
	 *
	 * @param id
	 * @return
	 */
	FileResponse copy(String id, String token);
	
	FileResponse getInfo(String id, String token);
	
	/**
	 * 网络 请求
	 *
	 * @param map
	 * @param token
	 * @return
	 */
	Result<?> post(String url,Map<String, Object> map, String token);
}
