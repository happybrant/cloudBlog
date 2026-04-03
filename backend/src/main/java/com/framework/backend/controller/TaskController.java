package com.framework.backend.controller;

import com.framework.backend.annotation.Log;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.entity.Progress;
import com.framework.backend.model.entity.Task;
import com.framework.backend.service.ProgressService;
import com.framework.backend.service.TaskService;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fucong
 */
@RestController
@RequestMapping("/task")
@ResponseResult
public class TaskController {

  @Autowired TaskService taskService;
  @Autowired ProgressService progressService;

  @GetMapping("/list")
  public List<Task> getTaskList() {
    return taskService.getTaskList();
  }

  /**
   * 新增任务
   *
   * @param task
   * @return
   */
  @PostMapping("/add")
  @Log(module = "任务管理", value = "新增任务")
  public String addTask(@RequestBody Task task) {
    taskService.addTask(task);
    return task.getId();
  }

  /**
   * 修改任务
   *
   * @param task
   * @return
   */
  @PostMapping("/update")
  @Log(module = "任务管理", value = "修改任务")
  public void updateTask(@RequestBody Task task) {
    taskService.updateTask(task);
  }

  /**
   * 根据id删除任务
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(module = "任务管理", value = "根据id删除任务")
  public void deleteTask(@RequestParam("id") String id) {
    taskService.deleteTask(id);
  }

  /**
   * 获取进度列表
   *
   * @param taskId
   * @return
   */
  @GetMapping("/progress/list")
  public List<Progress> getProgressList(@RequestParam("taskId") int taskId) {
    return progressService.getProgressList(taskId);
  }

  /**
   * 更新进度信息
   *
   * @param map
   * @return
   */
  @PostMapping("/progress/update")
  @Log(module = "任务管理", value = "更新任务进度")
  public void addProgress(@RequestBody Map<String, Object> map) {
    String content = map.get("content").toString();
    String taskId = map.get("taskId").toString();
    List<Integer> deleteProgressId = (List<Integer>) map.get("deleteProgressId");
    if (StringUtils.isEmpty(content)) {
      throw new BusinessException("参数为空，操作失败！");
    }
    Progress progress = new Progress(content, taskId, 1);
    progressService.addProgress(progress);
    if (!deleteProgressId.isEmpty()) {
      progressService.deleteProgressByIds(deleteProgressId);
    }
  }
}
