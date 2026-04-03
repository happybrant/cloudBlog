package com.framework.backend.controller;

import com.framework.backend.annotation.Log;
import com.framework.backend.model.vo.StatisticData;
import com.framework.backend.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 付聪
 */
@RestController
@RequestMapping("/home")
public class HomeController {
  @Autowired StatisticService statisticService;

  /**
   * 获取首页信息
   *
   * @return
   */
  @RequestMapping("getIndexData")
  @Log(module = "首页", value = "获取首页统计数据")
  public StatisticData getIndexData() {
    return statisticService.getStatisticData();
  }
}
