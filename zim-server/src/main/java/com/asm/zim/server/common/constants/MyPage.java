package com.asm.zim.server.common.constants;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author : azhao
 * @description
 */
public class MyPage<T> extends Page<T> {
	public MyPage(long current) {
		super(current, Constants.DEFAULT_PAGE_SIZE);
	}
	
	public MyPage(long current, long size) {
		super(current, size);
	}
	
	public MyPage(long current, long size, long total) {
		super(current, size, total);
	}
	
	public MyPage(long current, long size, boolean isSearchCount) {
		super(current, size, isSearchCount);
	}
	
	public MyPage(long current, long size, long total, boolean isSearchCount) {
		super(current, size, total, isSearchCount);
	}
}
