package com.framework.backend.controller;

import com.framework.backend.util.MinIoUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fuCong
 * @version 1.0.0 @Description 不受权限控制文件上传接口
 * @createTime 2022-06-13 12:39:00
 */
@RestController
@RequestMapping("/file")
public class FileController {

  @Autowired MinIoUtil minIoUtil;

  @Value("${minio.endpoint}")
  private String address;

  @Value("${minio.bucketName}")
  private String bucketName;

  /**
   * 上传文件
   *
   * @param file
   * @return
   */
  @PostMapping("/upload")
  public Object uploadFile(MultipartFile file) {

    List<String> upload = minIoUtil.upload(new MultipartFile[] {file});

    return address + "/" + bucketName + "/" + upload.get(0);
  }
}
