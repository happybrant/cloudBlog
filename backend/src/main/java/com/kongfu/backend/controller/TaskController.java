package com.kongfu.backend.controller;

import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.entity.Progress;
import com.kongfu.backend.model.entity.Task;
import com.kongfu.backend.service.ProgressService;
import com.kongfu.backend.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** @author fucong */
@RestController
@RequestMapping("/task")
public class TaskController {

  @Resource TaskService taskService;
  @Resource ProgressService progressService;

  @GetMapping("/list")
  @Log(menu = "任务管理", description = "获取任务列表")
  public ResponseResult<List<Task>> getTaskList() {
    List<Task> taskList = taskService.getTaskList();
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", taskList);
  }

  /**
   * 新增任务
   *
   * @param task
   * @return
   */
  @PostMapping("/add")
  @Log(menu = "任务管理", description = "新增任务")
  public ResponseResult<String> addTask(@RequestBody Task task) {
    ResponseResult<String> result;
    if (task == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = taskService.addTask(task);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 修改任务
   *
   * @param task
   * @return
   */
  @PostMapping("/update")
  @Log(menu = "任务管理", description = "修改任务")
  public ResponseResult<String> updateTask(@RequestBody Task task) {
    ResponseResult<String> result;
    if (task == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = taskService.updateTask(task);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 根据id删除任务
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(menu = "任务管理", description = "根据id删除任务")
  public ResponseResult<String> deleteTask(@RequestParam("id") Integer id) {
    ResponseResult<String> result;
    if (id == null || id == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = taskService.deleteTask(id);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 获取进度列表
   *
   * @param taskId
   * @return
   */
  @GetMapping("/progress/list")
  @Log(menu = "任务管理", description = "获取进度列表")
  public ResponseResult<List<Progress>> getProgressList(@RequestParam("taskId") int taskId) {
    if (taskId <= 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    List<Progress> progressList = progressService.getProgressList(taskId);
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", progressList);
  }

  /**
   * 更新进度信息
   *
   * @param map
   * @return
   */
  @PostMapping("/progress/update")
  @Log(menu = "任务管理", description = "更新任务进度")
  public ResponseResult<String> addProgress(@RequestBody Map<String, Object> map) {
    String content = map.get("content").toString();
    int taskId = Integer.parseInt(map.get("taskId").toString());
    List<Integer> deleteProgressId = (List<Integer>) map.get("deleteProgressId");
    ResponseResult<String> result;
    if (StringUtils.isEmpty(content) || taskId <= 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    Progress progress = new Progress(content, taskId);
    int i = progressService.addProgress(progress);
    String message = "";
    if (i > 0) {
      message = "成功添加" + i + "条数据";
    } else {
      return new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    if (deleteProgressId.size() > 0) {
      int j = progressService.deleteProgressByIds(deleteProgressId);
      if (j > 0) {
        message += ";成功删除" + j + "条数据";
      } else {
        return new ResponseResult<>(ResponseResultCode.Error, "操作失败");
      }
    }
    result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", message);
    return result;
  }
}
