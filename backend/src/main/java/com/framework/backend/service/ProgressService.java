package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.mapper.ProgressMapper;
import com.framework.backend.model.entity.Progress;
import com.framework.backend.util.BlogConstant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:38:00
 */
@Service
public class ProgressService {
  @Autowired private ProgressMapper progressMapper;

  /**
   * 新增任务进度
   *
   * @param progress
   */
  public void addProgress(Progress progress) {
    progressMapper.insert(progress);
  }

  /**
   * 根据任务id获取所有的进度
   *
   * @param taskId
   * @return
   */
  public List<Progress> getProgressList(int taskId) {
    QueryWrapper<Progress> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("task_id", taskId);
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    return progressMapper.selectList(queryWrapper);
  }

  public void deleteProgressByIds(List<Integer> ids) {
    progressMapper.deleteBatchIds(ids);
  }
}
