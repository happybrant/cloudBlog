package com.kongfu.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.AccessLogQuery;
import com.kongfu.backend.model.entity.AccessLog;
import com.kongfu.backend.service.AccessLogService;
import com.kongfu.backend.util.MapUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/** @author 付聪 */
@RestController
@RequestMapping("/accessLog")
public class LogController {
  @Resource public AccessLogService accessLogService;

  /**
   * 分页获取日志列表
   *
   * @param map
   * @return
   */
  @PostMapping("/list")
  @Log(menu = "系统管理/日志监控", description = "获取日志列表")
  public ResponseResult<Page<AccessLog>> getAccessLogList(
      @RequestBody(required = false) Map<String, Object> map) {
    Page<AccessLog> accessLogList = accessLogService.getAccessLogPager(getLogQuery(map));
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", accessLogList);
  }

  @GetMapping("/statistic")
  @Log(menu = "系统管理/日志监控", description = "获取日志统计数据")
  public Map<String, Long> getAccessLogCountByDay() {
    return accessLogService.getAccessLogCountByDay();
  }
  /**
   * 接口查询条件
   *
   * @param map
   * @return
   */
  private AccessLogQuery getLogQuery(Map<String, Object> map) {

    AccessLogQuery query = new AccessLogQuery();
    if (map == null || map.size() == 0) {
      query.setPageIndex(0);
      query.setPageSize(Integer.MAX_VALUE);
    } else {
      int pageIndex = MapUtil.getValueAsInteger(map, "pageIndex", 1);
      int pageSize = MapUtil.getValueAsInteger(map, "pageSize", 10);
      String keyword = MapUtil.getValueAsString(map, "keyword");
      String startTime = MapUtil.getValueAsString(map, "startTime");
      String endTime = MapUtil.getValueAsString(map, "endTime");
      int totalMills = MapUtil.getValueAsInteger(map, "totalMillis");
      query =
          new AccessLogQuery(
              keyword,
              startTime,
              endTime,
              totalMills,
              pageSize,
              pageIndex,
              pageIndex * (pageSize - 1));
    }
    return query;
  }
}
