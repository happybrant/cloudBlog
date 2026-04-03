package com.framework.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.backend.annotation.Log;
import com.framework.backend.common.MyPage;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.entity.Album;
import com.framework.backend.model.entity.Photo;
import com.framework.backend.service.AlbumService;
import com.framework.backend.service.PhotoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:39:00
 */
@RestController
@RequestMapping("/album")
@ResponseResult
public class AlbumController {
  @Autowired AlbumService albumService;
  @Autowired PhotoService photoService;

  /**
   * 获取相册列表
   *
   * @param pageIndex
   * @param pageSize
   * @return
   */
  @GetMapping("/list")
  public MyPage<Album> getAlbumList(
      @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) {
    return albumService.getAlbumList(pageIndex, pageSize);
  }

  /**
   * 新增相册
   *
   * @param album
   * @return
   */
  @PostMapping("/add")
  public String addAlbum(@RequestBody Album album) {
    albumService.addAlbum(album);
    return album.getId();
  }

  /**
   * 更新相册
   *
   * @param album
   */
  @PostMapping("/update")
  public void updateAlbum(@RequestBody Album album) {
    albumService.updateAlbum(album);
  }

  /**
   * 根据id删除相册
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  public void deleteAlbum(@RequestParam("id") String id) {
    albumService.deleteAlbum(id);
  }

  /**
   * 新增照片
   *
   * @param files
   * @param albumId
   */
  @PostMapping("/photo/add")
  @Log(module = "相册管理", value = "新增照片")
  public void addPhoto(
      @RequestParam("files") MultipartFile[] files, @RequestParam("albumId") Integer albumId) {
    List<String> fileNames = new ArrayList<>();
    List<Photo> photos = new ArrayList<>();
    photoService.addPhoto(photos);
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
  public Page<Photo> getPhotoList(
      @RequestParam("pageIndex") int pageIndex,
      @RequestParam("pageSize") int pageSize,
      @RequestParam("albumId") int albumId) {

    return photoService.getPhotoList(pageIndex, pageSize, albumId);
  }

  /**
   * @param id
   */
  @Log(module = "相册管理", value = "删除照片")
  @DeleteMapping("/photo/delete")
  public void deletePhoto(@RequestParam("id") Integer id) {
    photoService.deletePhoto(id);
  }
}
