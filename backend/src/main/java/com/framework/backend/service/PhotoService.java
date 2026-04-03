package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.backend.mapper.PhotoMapper;
import com.framework.backend.model.entity.Photo;
import com.framework.backend.util.BlogConstant;
import com.framework.backend.util.BlogUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:38:00
 */
@Service
public class PhotoService {
  @Autowired private PhotoMapper photoMapper;

  public void addPhoto(List<Photo> photos) {
    if (!photos.isEmpty()) {
      for (Photo photo : photos) {
        photoMapper.insert(photo);
      }
    }
  }

  /**
   * 根据相册id分页查看该相册下所有的照片
   *
   * @param pageIndex
   * @param pageSize
   * @param albumId
   * @return
   */
  public Page<Photo> getPhotoList(int pageIndex, int pageSize, int albumId) {
    QueryWrapper<Photo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("album_id", albumId);
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    Page<Photo> photoPage = new Page<>(pageIndex, pageSize);
    return photoMapper.selectPage(photoPage, queryWrapper);
  }

  /**
   * 根据id删除照片,逻辑删除
   *
   * @param id
   * @return
   */
  public void deletePhoto(int id) {
    Photo photo = photoMapper.selectById(id);
    if (photo != null) {
      photo.setStatus(BlogConstant.DELETE_STATUS);
      photoMapper.updateById(photo);
    }
  }

  /**
   * 计算相片数量
   *
   * @return
   */
  public long getPhotoCount() {
    QueryWrapper<Photo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    return photoMapper.selectCount(queryWrapper);
  }

  /**
   * 根据照片上传时间的月份对任务进行分组
   *
   * @return
   */
  public Map<String, Long> getPhotoByMonth() {
    QueryWrapper<Photo> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", BlogConstant.DELETE_STATUS);

    queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m')");
    queryWrapper.select(" DATE_FORMAT(create_time,'%Y-%m') AS Month, COUNT(*) AS Count");
    List<Map<String, Object>> taskList = photoMapper.selectMaps(queryWrapper);

    List<String> monthList = BlogUtil.getLatest12Month();

    Map<String, Long> photoMap = new LinkedHashMap<>(16);
    for (String month : monthList) {
      Map<String, Object> map =
          taskList.stream().filter(r -> r.get("Month").equals(month)).findAny().orElse(null);
      if (map == null) {
        photoMap.put(month, (long) 0);
      } else {
        photoMap.put(month, (Long) map.get("Count"));
      }
    }
    return photoMap;
  }
}
