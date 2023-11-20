package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.backend.dao.ProgressMapper;
import com.kongfu.backend.model.entity.Progress;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:38:00
 */
@Service
public class ProgressService {
  @Resource private ProgressMapper progressMapper;

  /**
   * 新增任务进度
   *
   * @param progress
   * @return
   */
  public int addProgress(Progress progress) {
    return progressMapper.insert(progress);
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

  public int deleteProgressByIds(List<Integer> ids) {
    return progressMapper.deleteBatchIds(ids);
  }
}
