package com.framework.backend.controller;

import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.entity.HostHolder;
import com.framework.backend.entity.IndexData;
import com.framework.backend.service.IndexService;
import com.framework.backend.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 付聪
 */
@RestController
@RequestMapping("/home")
@ResponseResult
public class IndexController {

  @Autowired public IndexService indexService;
  @Autowired public SettingService settingService;
  @Autowired private HostHolder holder;

  @GetMapping("/getIndexData")
  public IndexData getIndexData(@RequestParam("router") String router) {
    holder.setRouter(router);
    return indexService.getIndexData(router);
  }

  /**
   * 获取管理员路由
   *
   * @return
   */
  @GetMapping("/getDefaultRouting")
  public String getDefaultRouting() {
    return settingService.getDefaultRouting();
  }

  /**
   * 刷新统计数据缓存
   *
   * @param router
   * @return
   */
  @GetMapping("/refreshStatisticCache")
  public void refreshStatisticCache(@RequestParam String router) {
    indexService.calculateStatistic(router);
  }

  /**
   * 刷新设置数据缓存
   *
   * @param router
   * @return
   */
  @GetMapping("/refreshSettingCache")
  public void refreshSettingCache(String router) {
    indexService.calculateSetting(router);
  }

  /**
   * 重设缓存
   *
   * @param newRouter
   * @param oldRouter
   * @return
   */
  @GetMapping("/resetCache")
  public void resetCache(@RequestParam String newRouter, @RequestParam String oldRouter) {
    indexService.resetCache(newRouter, oldRouter);
  }
}
