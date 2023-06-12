package com.kongfu.backend.Config;

import com.kongfu.backend.controller.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/** @author 付聪 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

  @Resource private AuthInterceptor authInterceptor;

  /**
   * 对除静态资源的所有路径进行拦截
   *
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    super.addInterceptors(registry);

    // 设置拦截器拦截Url规则
    registry
        .addInterceptor(authInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/user/login/**", "/user/logout/**", "/file/upload/**", "/article/test");
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
        .favorParameter(true)
        .defaultContentType(MediaType.APPLICATION_JSON)
        .mediaType("xml", MediaType.APPLICATION_XML)
        .mediaType("json", MediaType.APPLICATION_JSON);
  }

  @Override
  protected void configurePathMatch(PathMatchConfigurer configurer) {
    // 设置请求Url忽略大小写
    AntPathMatcher matcher = new AntPathMatcher();
    matcher.setCaseSensitive(false);
    configurer.setPathMatcher(matcher);
  }
}
