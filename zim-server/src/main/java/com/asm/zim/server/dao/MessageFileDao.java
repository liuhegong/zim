package com.asm.zim.server.dao;

import com.asm.zim.server.entry.MessageFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author azhao
 * @description
 */
@Mapper
@Repository
public interface MessageFileDao extends BaseMapper<MessageFile> {

}
