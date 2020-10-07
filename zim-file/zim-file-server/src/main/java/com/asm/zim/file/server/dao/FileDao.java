package com.asm.zim.file.server.dao;

import com.asm.zim.file.server.entry.FilePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author : azhao
 * @description
 */
@Repository
@Mapper
public interface FileDao extends BaseMapper<FilePO> {
}
