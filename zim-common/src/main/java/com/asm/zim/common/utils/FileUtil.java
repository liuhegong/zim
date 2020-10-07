package com.asm.zim.common.utils;

/**
 * @author : azhao
 * @description
 */
public class FileUtil {
	/**
	 * 获取 后缀
	 *
	 * @param fileName
	 * @return
	 */
	public static String getType(String fileName) {
		if (fileName == null) {
			return "";
		}
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	
	public static String getIdByUrl(String path) {
		if (path==null){
			return "";
		}
		return path.substring(path.lastIndexOf("/")+1);
	}
	
}
