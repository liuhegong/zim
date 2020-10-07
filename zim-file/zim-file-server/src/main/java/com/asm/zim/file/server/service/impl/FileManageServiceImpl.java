package com.asm.zim.file.server.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.asm.zim.file.server.common.Constants;
import com.asm.zim.file.server.dao.FileDao;
import com.asm.zim.file.server.entry.FilePO;
import com.asm.zim.file.server.service.FileManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : azhao
 * @description
 */
@Service
public class FileManageServiceImpl implements FileManageService {
	private Logger logger = LoggerFactory.getLogger(FileManageServiceImpl.class);
	@Autowired
	private FileDao fileDao;
	
	@Override
	public FilePO getFilePo(String id) {
		return fileDao.selectById(id);
	}
	
	public void remove(String id) {
		if (id == null) {
			return;
		}
		FilePO filePO = fileDao.selectById(id);
		if (filePO != null) {
			String path = Constants.BASE_FILE_PATH + filePO.getPath();
			File file = new File(path);
			FileUtil.del(file);
			fileDao.deleteById(id);
		}
	}
	
	private FilePO saveFile(MultipartFile f) throws IOException {
		FilePO fileEntry = new FilePO();
		fileEntry.setId(IdUtil.fastSimpleUUID());
		fileEntry.setCreateTime(System.currentTimeMillis());
		String fileName = f.getOriginalFilename() == null ? "" : f.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		fileEntry.setFileName(f.getOriginalFilename());
		fileEntry.setSuffix(suffix);
		fileEntry.setSize(f.getSize());
		String path = File.separator + fileEntry.getId() + "." + suffix;
		String realPath = Constants.BASE_FILE_PATH + path;
		File file = new File(realPath);
		f.transferTo(file);
		fileEntry.setPath(path);
		// fileEntry.getPersonId();
		// fileEntry.setModifyTime();
		// fileEntry.setModule();
		return fileEntry;
	}
	
	public List<FilePO> upload(MultipartFile[] files) throws IOException {
		List<FilePO> fileList = new LinkedList<>();
		if (files != null) {
			for (MultipartFile f : files) {
				FilePO fileEntry = saveFile(f);
				fileList.add(fileEntry);
				fileDao.insert(fileEntry);
			}
		}
		return fileList;
	}
	
	@Override
	public FilePO upload(MultipartFile file) throws IOException {
		FilePO fileEntry = saveFile(file);
		fileDao.insert(fileEntry);
		return fileEntry;
	}
	
	
	@Override
	public FilePO copy(String id) {
		FilePO filePO = fileDao.selectById(id);
		if (filePO == null) {
			logger.info("文件不存在");
			return null;
		}
		File oldFile = new File(Constants.BASE_FILE_PATH + filePO.getPath());
		String newId = IdUtil.fastSimpleUUID();
		String newPath = File.separator + newId + "." + filePO.getSuffix();
		String realNewPath = Constants.BASE_FILE_PATH + newPath;
		File newFile = new File(realNewPath);
		FileUtil.copy(oldFile, newFile, false);
		FilePO newFilePo = filePO.clone();
		newFilePo.setId(newId);
		newFilePo.setCreateTime(System.currentTimeMillis());
		newFilePo.setPath(newPath);
		fileDao.insert(newFilePo);
		return newFilePo;
	}
}
