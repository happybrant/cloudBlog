package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.common.MyPage;
import com.framework.backend.mapper.AlbumMapper;
import com.framework.backend.model.entity.Album;
import com.framework.backend.util.BlogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:19:00
 */
@Service
public class AlbumService {
  @Autowired private AlbumMapper albumMapper;

  /**
   * 新增相册
   *
   * @param album
   */
  public void addAlbum(Album album) {
    albumMapper.insert(album);
  }

  /**
   * 根据id更新相册
   *
   * @param album
   */
  public void updateAlbum(Album album) {
    albumMapper.updateById(album);
  }

  /**
   * 根据id删除相册,逻辑删除
   *
   * @param id
   */
  public void deleteAlbum(String id) {
    Album album = albumMapper.selectById(id);
    if (album != null) {
      // 将状态修改为0
      album.setStatus(BlogConstant.DELETE_STATUS);
      albumMapper.updateById(album);
    }
  }

  /**
   * 获取相册列表
   *
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public MyPage<Album> getAlbumList(int pageIndex, int pageSize) {
    QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    MyPage<Album> albumPage = new MyPage<>(pageIndex, pageSize);
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
