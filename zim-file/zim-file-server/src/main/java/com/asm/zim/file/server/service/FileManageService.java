package com.asm.zim.file.server.service;

import com.asm.zim.file.server.entry.FilePO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author : azhao
 * @description
 */
public interface FileManageService {
	void remove(String id);
	
	FilePO getFilePo(String id);
	
	List<FilePO> upload(MultipartFile[] files) throws IOException;
	
	FilePO upload(MultipartFile file) throws IOException;
	
	FilePO copy(String id);
	
}
