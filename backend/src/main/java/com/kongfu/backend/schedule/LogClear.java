package com.kongfu.backend.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.backend.model.entity.AccessLog;
import com.kongfu.backend.model.entity.Entity;
import com.kongfu.backend.service.AccessLogService;
import com.kongfu.backend.util.BlogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/** @Author fuCong @Date 2023/10/27 10:59 定期清除日志 */
@Component
@EnableScheduling
@Slf4j
public class LogClear {
  @Resource private AccessLogService accessLogService;

  /** 定时删除日志，一天删除一次 */
  @Scheduled(initialDelay = 10000, fixedRate = 24 * 60 * 60 * 1000)
  public void process() {
    // 获取前1个月的时间
    String lastMonthDay = BlogUtil.getLateNDay(30);
    QueryWrapper<AccessLog> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().le(Entity::getCreateTime, lastMonthDay);
    // 删除一天前的日志
    accessLogService.remove(queryWrapper);
    log.info("删除【" + lastMonthDay + "】前的日志完成");
  }
}
