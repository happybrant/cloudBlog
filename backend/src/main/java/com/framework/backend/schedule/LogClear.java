package com.framework.backend.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.common.MyBaseEntity;
import com.framework.backend.model.entity.SecurityLog;
import com.framework.backend.service.LogService;
import com.framework.backend.util.BlogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author fuCong @Date 2023/10/27 10:59 定期清除日志
 */
@EnableScheduling
@Slf4j
public class LogClear {
  @Autowired private LogService logService;

  /** 定时删除日志，一天删除一次 */
  @Scheduled(initialDelay = 10000, fixedRate = 24 * 60 * 60 * 1000)
  public void process() {
    // 获取前1个月的时间
    String lastMonthDay = BlogUtil.getLateNDay(30);
    QueryWrapper<SecurityLog> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().le(MyBaseEntity::getCreateTime, lastMonthDay);
    // 删除一天前的日志
    logService.remove(queryWrapper);
    log.info("删除【" + lastMonthDay + "】前的日志完成");
  }
}
