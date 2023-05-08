package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.backend.dao.AccessLogMapper;
import com.kongfu.backend.model.entity.AccessLog;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** @Author fuCong @Date 2023/4/28 10:20 */
@Service
public class AccessLogService {
  @Resource private AccessLogMapper accessLogMapper;

  public int addAccessLog(AccessLog accessLog) {
    accessLog.setStatus(BlogConstant.PUBLISH_STATUS);
    return accessLogMapper.insert(accessLog);
  }

  /**
   * 获取日志列表
   *
   * @return
   */
  public List<AccessLog> getAccessLogList() {
    QueryWrapper<AccessLog> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", BlogConstant.TASK_DELETE_STATUS);
    return accessLogMapper.selectList(queryWrapper);
  }
}
