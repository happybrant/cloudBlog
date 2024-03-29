package com.kongfu.backend.Config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** @Author fuCong @Date 2023/6/11 12:11 */
@Configuration
public class RestTemplateConfig {

  /**
   * 没有实例化RestTemplate时，初始化RestTemplate
   *
   * @return
   */
  @ConditionalOnMissingBean(RestTemplate.class)
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
