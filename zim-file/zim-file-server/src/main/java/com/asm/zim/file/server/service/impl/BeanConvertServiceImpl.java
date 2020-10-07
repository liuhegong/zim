package com.asm.zim.file.server.service.impl;

import com.asm.zim.common.entry.FileRequest;
import com.asm.zim.common.entry.FileResponse;
import com.asm.zim.file.server.entry.FilePO;
import com.asm.zim.file.server.service.BeanConvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author : azhao
 * @description
 */
@Service
public class BeanConvertServiceImpl implements BeanConvertService {
	private Logger logger = LoggerFactory.getLogger(BeanConvertServiceImpl.class);
	
	@Override
	public FileRequest filePoToFileRequest(FilePO filePO) {
		if (filePO == null) {
			return null;
		}
		FileRequest fileEntry = new FileRequest();
		fileEntry.setId(filePO.getId());
		fileEntry.setCreateTime(filePO.getCreateTime());
		fileEntry.setSize(filePO.getSize());
		fileEntry.setSuffix(filePO.getSuffix());
		fileEntry.setFileName(filePO.getFileName());
		return fileEntry;
	}
	
	@Override
	public FileResponse filePoToFileResponse(FilePO filePO) {
		if (filePO == null) {
			return null;
		}
		FileResponse fileResponse = new FileResponse();
		fileResponse.setId(filePO.getId());
		fileResponse.setCreateTime(filePO.getCreateTime());
		fileResponse.setSize(filePO.getSize());
		fileResponse.setSuffix(filePO.getSuffix());
		fileResponse.setFileName(filePO.getFileName());
		fileResponse.setUrl("/file/download/" + filePO.getId());
		return fileResponse;
	}
}
