package com.asm.zim.server.dao;

import com.asm.zim.server.entry.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : azhao
 * @description
 */
@Repository
@Mapper
public interface FriendDao extends BaseMapper<Friend> {
	List<Friend> selectGroupFriend(String personId, String groupId);
}
