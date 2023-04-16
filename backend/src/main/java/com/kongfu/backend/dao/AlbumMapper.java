package com.kongfu.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongfu.backend.model.entity.Album;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:22:00
 */
@Mapper
public interface AlbumMapper extends BaseMapper<Album> {
}
