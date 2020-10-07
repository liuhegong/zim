package com.asm.zim.file.server.service;

import com.asm.zim.common.entry.FileRequest;
import com.asm.zim.common.entry.FileResponse;
import com.asm.zim.file.server.entry.FilePO;

/**
 * @author : azhao
 * @description
 */
public interface BeanConvertService {
	FileRequest filePoToFileRequest(FilePO filePO);
	
	FileResponse filePoToFileResponse(FilePO filePO);
}
