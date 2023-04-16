package com.kongfu.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.entity.Album;
import com.kongfu.backend.model.entity.Photo;
import com.kongfu.backend.service.AlbumService;
import com.kongfu.backend.service.PhotoService;
import com.kongfu.backend.util.MinIoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:39:00
 */
@RestController
@RequestMapping("/album")
public class AlbumController {
  @Resource AlbumService albumService;
  @Resource PhotoService photoService;
  @Resource MinIoUtil minIoUtil;

  @Value("${minio.endpoint}")
  private String address;

  @Value("${minio.bucketName}")
  private String bucketName;

  /**
   * 获取相册列表
   *
   * @param pageIndex
   * @param pageSize
   * @return
   */
  @GetMapping("/list")
  public ResponseResult<Page<Album>> getAlbumList(
      @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) {
    if (pageIndex <= 0 || pageSize <= 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    Page<Album> albumPage = albumService.getAlbumList(pageIndex, pageSize);
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", albumPage);
  }

  /**
   * 新增相册
   *
   * @param album
   * @return
   */
  @PostMapping("/add")
  public ResponseResult<String> addAlbum(@RequestBody Album album) {
    ResponseResult<String> result;
    if (album == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = albumService.addAlbum(album);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 更新相册
   *
   * @param album
   * @return
   */
  @PostMapping("/update")
  public ResponseResult<String> updateAlbum(@RequestBody Album album) {
    ResponseResult<String> result;
    if (album == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = albumService.updateAlbum(album);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 根据id删除相册
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  public ResponseResult<String> deleteAlbum(@RequestParam("id") Integer id) {
    ResponseResult<String> result;
    if (id == null || id == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = albumService.deleteAlbum(id);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 新增照片
   *
   * @param files
   * @param albumId
   * @return
   */
  @PostMapping("/photo/add")
  public ResponseResult<String> addPhoto(MultipartFile[] files, Integer albumId) {
    ResponseResult<String> result;
    if (files == null || files.length <= 0 || albumId == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，添加失败");
    }
    List<String> fileNames = minIoUtil.upload(files);
    List<Photo> photos = new ArrayList<>();
    if (fileNames.size() > 0) {
      for (String fileName : fileNames) {
        String path = address + "/" + bucketName + "/" + fileName;
        Photo photo = new Photo(path, albumId, "");
        photos.add(photo);
      }
    } else {
      return new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }

    int i = photoService.addPhoto(photos);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 获取照片列表
   *
   * @param pageIndex
   * @param pageSize
   * @param albumId
   * @return
   */
  @GetMapping("/photo/list")
  public ResponseResult<Page<Photo>> getPhotoList(
      @RequestParam("pageIndex") int pageIndex,
      @RequestParam("pageSize") int pageSize,
      @RequestParam("albumId") int albumId) {
    if (pageIndex <= 0 || pageSize <= 0 || albumId <= 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    Page<Photo> photoPage = photoService.getPhotoList(pageIndex, pageSize, albumId);
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", photoPage);
  }

  /**
   * 根据id删除相册
   *
   * @param id
   * @return
   */
  @DeleteMapping("/photo/delete")
  public ResponseResult<String> deletePhoto(@RequestParam("id") Integer id) {
    ResponseResult<String> result;
    if (id == null || id == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = photoService.deletePhoto(id);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }
}
