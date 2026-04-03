package com.framework.backend.controller;

import com.framework.backend.annotation.Log;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.entity.Plan;
import com.framework.backend.service.PlanService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fucong
 */
@RestController
@RequestMapping("/plan")
@ResponseResult
public class PlanController {

  @Autowired PlanService planService;

  @GetMapping("/list")
  public List<Plan> getPlanList() {
    return planService.getPlanList();
  }

  /**
   * 新增计划
   *
   * @param plan
   * @return
   */
  @PostMapping("/add")
  @Log(module = "计划管理", value = "新增计划")
  public String addPlan(@RequestBody Plan plan) {
    planService.addPlan(plan);
    return plan.getId();
  }

  /**
   * 修改计划
   *
   * @param plan
   * @return
   */
  @PostMapping("/update")
  @Log(module = "计划管理", value = "修改计划")
  public void updatePlan(@RequestBody Plan plan) {
    planService.updatePlan(plan);
  }

  /**
   * 根据id删除计划
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(module = "计划管理", value = "删除计划")
  public void deletePlan(@RequestParam("id") String id) {
    planService.deletePlan(id);
  }
}
