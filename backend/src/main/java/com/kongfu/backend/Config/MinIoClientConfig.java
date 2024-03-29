package com.kongfu.backend.Config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-01 16:50:00
 */
@Configuration
public class MinIoClientConfig {
  @Value("${minio.endpoint}")
  private String endpoint;

  @Value("${minio.accessKey}")
  private String accessKey;

  @Value("${minio.secretKey}")
  private String secretKey;

  /**
   * 注入minio 客户端
   *
   * @return
   */
  @Bean
  public MinioClient minioClient() {

    return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
  }
}
