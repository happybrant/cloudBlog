package com.kongfu.backend.dao;

import com.kongfu.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 付聪
 */
@Mapper
public interface UserMapper {

    /**
     * 根据 username 查询用户
     * @param username
     * @return
     */
    User selectByName(String username);

    /**
     * 根据 userId 查询用户
     * @param userId
     * @return
     */
    User selectByUserId(int userId);
}
