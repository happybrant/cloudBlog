package com.kongfu.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** @author 付聪 */
@SpringBootApplication
// @EnableEurekaClient
/** 配置自动扫描路径 */
@EnableTransactionManagement
public class BackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }
}
