package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.dao.AlbumMapper;
import com.kongfu.backend.model.entity.Album;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:19:00
 */
@Service
public class AlbumService {
  @Resource private AlbumMapper albumMapper;
  @Resource private HostHolder holder;

  /**
   * 新增相册
   *
   * @param album
   * @return
   */
  public int addAlbum(Album album) {
    return albumMapper.insert(album);
  }

  /**
   * 根据id更新相册
   *
   * @param album
   * @return
   */
  public int updateAlbum(Album album) {
    return albumMapper.updateById(album);
  }

  /**
   * 根据id删除相册,逻辑删除
   *
   * @param id
   * @return
   */
  public int deleteAlbum(int id) {
    Album album = albumMapper.selectById(id);
    if (album != null) {
      // 将状态修改为0
      album.setStatus(BlogConstant.DELETE_STATUS);
      return albumMapper.updateById(album);
    }
    return 0;
  }

  /**
   * 获取相册列表
   *
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public Page<Album> getAlbumList(int pageIndex, int pageSize) {
    QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    Page<Album> albumPage = new Page<>(pageIndex, pageSize);
    return albumMapper.selectPage(albumPage, queryWrapper);
  }

  /**
   * 计算相册数量
   *
   * @return
   */
  public long getAlbumCount() {
    QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    return albumMapper.selectCount(queryWrapper);
  }
}
