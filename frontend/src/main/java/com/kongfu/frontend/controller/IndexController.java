package com.kongfu.frontend.controller;

import com.kongfu.frontend.common.ResponseResult;
import com.kongfu.frontend.common.ResponseResultCode;
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

  /**
   * 刷新统计数据缓存
   *
   * @param router
   * @return
   */
  @GetMapping("/refreshStatisticCache")
  public ResponseResult refreshStatisticCache(@RequestParam String router) {
    indexService.calculateStatistic(router);
    return new ResponseResult(ResponseResultCode.Success, "统计数据缓存刷新成功");
  }

  /**
   * 刷新设置数据缓存
   *
   * @param router
   * @return
   */
  @GetMapping("/refreshSettingCache")
  public ResponseResult refreshSettingCache(String router) {
    indexService.calculateSetting(router);
    return new ResponseResult(ResponseResultCode.Success, "设置数据缓存刷新成功");
  }

  /**
   * 重设缓存
   *
   * @param newRouter
   * @param oldRouter
   * @return
   */
  @GetMapping("/resetCache")
  public ResponseResult resetCache(@RequestParam String newRouter, @RequestParam String oldRouter) {
    indexService.resetCache(newRouter, oldRouter);
    return new ResponseResult(ResponseResultCode.Success, "缓存重设成功");
  }
}
