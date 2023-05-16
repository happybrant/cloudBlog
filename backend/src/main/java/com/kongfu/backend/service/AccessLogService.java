package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.dao.AccessLogMapper;
import com.kongfu.backend.model.dto.AccessLogQuery;
import com.kongfu.backend.model.entity.AccessLog;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** @Author fuCong @Date 2023/4/28 10:20 */
@Service
public class AccessLogService {
  @Resource private AccessLogMapper accessLogMapper;

  public int addAccessLog(AccessLog accessLog) {
    accessLog.setStatus(BlogConstant.PUBLISH_STATUS);
    return accessLogMapper.insert(accessLog);
  }
  /**
   * 博客列表，支持分页和条件查询
   *
   * @return
   */
  public Page<AccessLog> getAccessLogPager(AccessLogQuery query) {
    Page<AccessLog> accessLogPage = new Page<>(query.getPageIndex(), query.getPageSize());
    QueryWrapper<AccessLog> queryWrapper = new QueryWrapper<>();
    if (StringUtils.isNotBlank(query.getStartTime())) {
      queryWrapper.ge("request_time", query.getStartTime());
    }
    if (StringUtils.isNotBlank(query.getStartTime())) {
      queryWrapper.le("request_time", query.getEndTime());
    }
    if (query.getTotalMillis() > 0) {
      queryWrapper.ge("totalMillis", query.getTotalMillis());
    }
    if (StringUtils.isNotBlank(query.getKeyword())) {
      String keyword = query.getKeyword();
      queryWrapper.and(
          wrapper ->
              wrapper
                  .like("description", keyword)
                  .or()
                  .like("menu", keyword)
                  .or()
                  .like("ip", keyword));
    }
    queryWrapper.orderByDesc("request_time");
    return accessLogMapper.selectPage(accessLogPage, queryWrapper);
  }

  /**
   * 统计近30天日志数据
   *
   * @return
   */
  public Map<String, Long> getAccessLogCountByDay() {

    // 获取最近30天的开始和结束时间
    String[] times = getTimeBoundary();
    QueryWrapper<AccessLog> queryWrapper = new QueryWrapper<>();

    queryWrapper.le("create_time", times[0]);
    queryWrapper.ge("create_time", times[1]);
    queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m-%d')");
    queryWrapper.select("DATE_FORMAT(create_time,'%Y-%m-%d') AS Day, COUNT(*) AS Count");

    List<Map<String, Object>> logList = accessLogMapper.selectMaps(queryWrapper);

    return groupLogCountByDate(logList);
  }

  /**
   * 获取最近一个月的开始和结束时间
   *
   * @return
   */
  public String[] getTimeBoundary() {
    String[] times = new String[2];
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // 结束时间
    times[0] = dateFormat.format(new Date()) + " 23:59:59";
    // 获取30天前的日期
    calendar.add(Calendar.DATE, -30);
    Date lastWeekday = calendar.getTime();
    // 开始时间
    times[1] = dateFormat.format(lastWeekday) + " 00:00:00";
    return times;
  }

  /**
   * 根据日期对查询出最近一周的任务数量进行分组
   *
   * @param logList
   * @return
   */
  Map<String, Long> groupLogCountByDate(List<Map<String, Object>> logList) {
    // 用于存放近30天的日志数量
    Map<String, Long> logCountList = new LinkedHashMap<>();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // 最近一周的日期集合
    List<Date> dateList = BlogUtil.getLatestDays(30);
    for (Date date : dateList) {
      Map<String, Object> map =
          logList.stream()
              .filter(r -> r.get("Day").equals(dateFormat.format(date)))
              .findAny()
              .orElse(null);
      if (map == null) {
        logCountList.put(dateFormat.format(date), (long) 0);
      } else {
        logCountList.put(dateFormat.format(date), (Long) map.get("Count"));
      }
    }
    return logCountList;
  }
}
