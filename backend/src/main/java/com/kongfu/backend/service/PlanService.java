package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.backend.dao.PlanMapper;
import com.kongfu.backend.model.entity.Plan;
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
public class PlanService {
  @Resource private PlanMapper planMapper;

  public int addPlan(Plan plan) {
    return planMapper.insert(plan);
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
  public int updatePlan(Plan plan) {
    return planMapper.updateById(plan);
  }

  /**
   * 根据id删除任务，逻辑删除
   *
   * @param id
   * @return
   */
  public int deletePlan(int id) {
    Plan plan = planMapper.selectById(id);
    if (plan != null) {
      plan.setStatus(BlogConstant.TASK_DELETE_STATUS);
      return planMapper.updateById(plan);
    }
    return 0;
  }
}
