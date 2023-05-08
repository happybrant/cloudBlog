package com.kongfu.backend.controller;

import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.vo.StatisticData;
import com.kongfu.backend.service.StatisticService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** @author 付聪 */
@RestController
@RequestMapping("/home")
public class HomeController {
  @Resource StatisticService statisticService;

  /**
   * 获取首页信息
   *
   * @return
   */
  @RequestMapping("getIndexData")
  @Log(menu = "首页", description = "获取首页统计数据")
  public ResponseResult<StatisticData> getIndexData() {

    StatisticData statisticData = statisticService.getStatisticData();
    return new ResponseResult<>(ResponseResultCode.Success, "查询成功", statisticData);
  }
}
