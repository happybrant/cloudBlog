package com.kongfu.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** @Author fuCong @Date 2022/12/1 19:11 支持跨域 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedHeaders("*")
        .allowedMethods("*")
        .allowedOriginPatterns("*")
        .allowCredentials(true);
  }

  /** 支持PUT、DELETE请求 */
  @Bean
  public FormContentFilter httpPutFormContentFilter() {
    return new FormContentFilter();
  }
}
