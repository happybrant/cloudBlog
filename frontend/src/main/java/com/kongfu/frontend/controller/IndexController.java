package com.kongfu.frontend.controller;

import com.kongfu.frontend.entity.HostHolder;
import com.kongfu.frontend.entity.IndexData;
import com.kongfu.frontend.service.IndexService;
import com.kongfu.frontend.service.SettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** @author 付聪 */
@RestController
@RequestMapping("/home")
public class IndexController {

  @Resource public IndexService indexService;
  @Resource public SettingService settingService;
  @Resource private HostHolder holder;

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

  /** 提供查询最新的接口给后台管理服务 */
  @GetMapping("/refreshStatisticCache")
  public String refreshStatisticCache(@RequestParam String router) {
    indexService.calculateStatistic(router);
    return "success";
  }

  public void refreshSettingCache(String router) {
    indexService.calculateSetting(router);
  }
}
