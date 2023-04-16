package com.kongfu.backend.controller;

import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.entity.Plan;
import com.kongfu.backend.service.PlanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author fucong */
@RestController
@RequestMapping("/plan")
public class PlanController {

  @Resource PlanService planService;

  @GetMapping("/list")
  public ResponseResult<List<Plan>> getPlanList() {
    List<Plan> planList = planService.getPlanList();
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", planList);
  }

  /**
   * 新增计划
   *
   * @param plan
   * @return
   */
  @PostMapping("/add")
  public ResponseResult<String> addTask(@RequestBody Plan plan) {
    ResponseResult<String> result;
    if (plan == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = planService.addPlan(plan);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 修改计划
   *
   * @param plan
   * @return
   */
  @PostMapping("/update")
  public ResponseResult<String> updateTask(@RequestBody Plan plan) {
    ResponseResult<String> result;
    if (plan == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = planService.updatePlan(plan);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 根据id删除计划
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  public ResponseResult<String> deleteTask(@RequestParam("id") Integer id) {
    ResponseResult<String> result;
    if (id == null || id == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = planService.deletePlan(id);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }
}
