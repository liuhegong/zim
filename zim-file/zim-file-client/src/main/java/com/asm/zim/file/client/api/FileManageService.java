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
	void removeFile(String id);
	
	/**
	 * @param url   真实url
	 * @param list
	 * @return
	 */
	List<FileResponse> upload(String url, List<FileRequest> list);
	
	/**
	 * @param url       真实url
	 * @param fileEntry
	 * @return url
	 */
	FileResponse upload(String url, FileRequest fileEntry);
	
	/**
	 * @param uploadFileEntry
	 * @return 返回 url
	 */
	FileResponse upload(FileRequest uploadFileEntry);
	
	
	List<FileResponse> upload(List<FileRequest> list);
	
	void download(String id,  HttpServletResponse response);
	
	/**
	 * 根据 文件 id 复制
	 *
	 * @param id
	 * @return
	 */
	FileResponse copy(String id);
	
	FileResponse getInfo(String id);
	
	/**
	 * 网络 请求
	 *
	 * @param map
	 * @return
	 */
	Result<?> post(String url,Map<String, Object> map);
}
