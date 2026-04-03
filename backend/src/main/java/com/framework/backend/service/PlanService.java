package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.mapper.PlanMapper;
import com.framework.backend.model.entity.Plan;
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
public class PlanService {
  @Autowired private PlanMapper planMapper;

  public void addPlan(Plan plan) {
    planMapper.insert(plan);
  }

  /**
   * 获取任务列表
   *
   * @return
   */
  public List<Plan> getPlanList() {
    QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", BlogConstant.TASK_DELETE_STATUS);
    queryWrapper.orderByAsc("order");
    return planMapper.selectList(queryWrapper);
  }

  /**
   * 根据id更新任务
   *
   * @param plan
   * @return
   */
  public void updatePlan(Plan plan) {
    planMapper.updateById(plan);
  }

  /**
   * 根据id删除任务，逻辑删除
   *
   * @param id
   * @return
   */
  public void deletePlan(String id) {
    Plan plan = planMapper.selectById(id);
    if (plan != null) {
      plan.setStatus(BlogConstant.TASK_DELETE_STATUS);
      planMapper.updateById(plan);
    }
  }
}
