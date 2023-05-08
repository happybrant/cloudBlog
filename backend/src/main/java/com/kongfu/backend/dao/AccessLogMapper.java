package com.kongfu.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongfu.backend.model.entity.AccessLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2023-04-28 10:23:00
 */
@Mapper
public interface AccessLogMapper extends BaseMapper<AccessLog> {}
