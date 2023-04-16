package com.kongfu.frontend.entity;

import org.springframework.stereotype.Component;

/** @author 付聪 持有router(多线程)，用于代替 session 对象 */
@Component
public class HostHolder {

  private ThreadLocal<String> routers = new ThreadLocal<>();

  /** 存储 Router */
  public void setRouter(String router) {
    routers.set(router);
  }

  /**
   * 获取Router
   *
   * @return
   */
  public String getRouter() {
    return routers.get();
  }

  /** 清理 */
  public void clear() {
    routers.remove();
  }
}
