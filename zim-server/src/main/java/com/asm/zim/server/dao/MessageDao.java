package com.asm.zim.server.dao;

import com.asm.zim.server.entry.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author : azhao
 * @description
 */
@Repository
@Mapper
public interface MessageDao extends BaseMapper<Message> {
	/**
	 * map
	 * personId
	 * messageType
	 * chatType
	 * terminalType
	 *
	 * @param page
	 * @param map
	 * @return
	 */
	IPage<Message> selectMessageList(Page<Message> page, @Param("map") Map<String, Object> map);
	
	
	void setHasRead(@Param("map") Map<String, Object> map);
}
