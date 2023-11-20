package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.dao.PhotoMapper;
import com.kongfu.backend.model.entity.Photo;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:38:00
 */
@Service
public class PhotoService {
  @Resource private PhotoMapper photoMapper;

  public int addPhoto(List<Photo> photos) {
    int count = 0;
    if (photos.size() > 0) {
      for (Photo photo : photos) {
        int num = photoMapper.insert(photo);
        count += num;
      }
    }
    return count;
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
  public int deletePhoto(int id) {
    Photo photo = photoMapper.selectById(id);
    if (photo != null) {
      photo.setStatus(BlogConstant.DELETE_STATUS);
      return photoMapper.updateById(photo);
    }
    return 0;
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
