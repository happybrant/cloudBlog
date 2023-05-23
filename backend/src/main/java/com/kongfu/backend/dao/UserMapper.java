package com.kongfu.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongfu.backend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/** @author 付聪 */
@Mapper
public interface UserMapper extends BaseMapper<User> {}
