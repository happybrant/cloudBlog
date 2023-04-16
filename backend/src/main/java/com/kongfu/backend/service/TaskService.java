package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.backend.dao.TaskMapper;
import com.kongfu.backend.model.entity.Task;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:38:00
 */
@Service
public class TaskService {
  @Resource private TaskMapper taskMapper;

  public int addTask(Task task) {
    task.setStatus(BlogConstant.PUBLISH_STATUS);
    return taskMapper.insert(task);
  }

  /**
   * 获取任务列表
   *
   * @return
   */
  public List<Task> getTaskList() {
    QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", BlogConstant.TASK_DELETE_STATUS);
    queryWrapper.orderByAsc("end_time", "level", "start_time");
    return taskMapper.selectList(queryWrapper);
  }

  /**
   * 根据id更新任务
   *
   * @param task
   * @return
   */
  public int updateTask(Task task) {
    return taskMapper.updateById(task);
  }

  /**
   * 根据id删除任务，逻辑删除
   *
   * @param id
   * @return
   */
  public int deleteTask(int id) {
    Task task = taskMapper.selectById(id);
    if (task != null) {
      task.setStatus(BlogConstant.TASK_DELETE_STATUS);
      return taskMapper.updateById(task);
    }
    return 0;
  }

  /**
   * 统计最近一年每月创建的任务数量
   *
   * @return
   */
  public Map<String, Long> getCreatedTaskByMonth() {
    QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

    queryWrapper.ne("status", BlogConstant.TASK_DELETE_STATUS);
    queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m')");
    queryWrapper.select(" DATE_FORMAT(create_time,'%Y-%m') AS Month, COUNT(*) AS Count");
    List<Map<String, Object>> taskList = taskMapper.selectMaps(queryWrapper);

    return groupTaskCountByMonth(taskList);
  }

  /**
   * 统计最近一年每月开始执行的任务数量
   *
   * @return
   */
  public Map<String, Long> getStartedTaskByMonth() {
    QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

    queryWrapper.gt("status", BlogConstant.TASK_TODO_STATUS);
    queryWrapper.groupBy("DATE_FORMAT(start_time,'%Y-%m')");
    queryWrapper.select(" DATE_FORMAT(start_time,'%Y-%m') AS Month, COUNT(*) AS Count");
    List<Map<String, Object>> taskList = taskMapper.selectMaps(queryWrapper);

    return groupTaskCountByMonth(taskList);
  }

  /**
   * 统计最近一年每月完成的任务数量
   *
   * @return
   */
  public Map<String, Long> getFinishedTaskByMonth() {
    QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

    queryWrapper.eq("status", BlogConstant.TASK_DONE_STATUS);
    queryWrapper.groupBy("DATE_FORMAT(end_time,'%Y-%m')");
    queryWrapper.select(" DATE_FORMAT(end_time,'%Y-%m') AS Month, COUNT(*) AS Count");

    List<Map<String, Object>> taskList = taskMapper.selectMaps(queryWrapper);

    return groupTaskCountByMonth(taskList);
  }

  /**
   * 统计最近一周每天创建的任务数量
   *
   * @return
   */
  public Map<String, Long> getCreatedTaskByDay() {
    // 存放最近一周每天创建的任务数量
    List<Long> taskCountList = new ArrayList<>();
    // 获取最近一周的开始和结束时间
    String[] times = getTimeBoundary();
    QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

    queryWrapper.ne("status", BlogConstant.TASK_DELETE_STATUS);
    queryWrapper.le("create_time", times[0]);
    queryWrapper.ge("create_time", times[1]);
    queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m-%d')");
    queryWrapper.select("DATE_FORMAT(create_time,'%Y-%m-%d') AS Day, COUNT(*) AS Count");

    List<Map<String, Object>> taskList = taskMapper.selectMaps(queryWrapper);

    return groupTaskCountByDate(taskList);
  }

  /**
   * 统计最近一周每天开始执行的任务数量
   *
   * @return
   */
  public Map<String, Long> getStartedTaskByDay() {
    // 获取最近一周的开始和结束时间
    String[] times = getTimeBoundary();
    QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

    queryWrapper.gt("status", BlogConstant.TASK_TODO_STATUS);
    queryWrapper.le("start_time", times[0]);
    queryWrapper.ge("start_time", times[1]);
    queryWrapper.groupBy("DATE_FORMAT(start_time,'%Y-%m-%d')");
    queryWrapper.select("DATE_FORMAT(start_time,'%Y-%m-%d') AS Day, COUNT(*) AS Count");

    List<Map<String, Object>> taskList = taskMapper.selectMaps(queryWrapper);

    return groupTaskCountByDate(taskList);
  }

  /**
   * 统计最近一周每天完成的任务数量
   *
   * @return
   */
  public Map<String, Long> getFinishedTaskByDay() {
    // 获取最近一周的开始和结束时间
    String[] times = getTimeBoundary();
    QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

    queryWrapper.eq("status", BlogConstant.TASK_DONE_STATUS);
    queryWrapper.le("end_time", times[0]);
    queryWrapper.ge("end_time", times[1]);
    queryWrapper.groupBy("DATE_FORMAT(end_time,'%Y-%m-%d')");
    queryWrapper.select("DATE_FORMAT(end_time,'%Y-%m-%d') AS Day, COUNT(*) AS Count");

    List<Map<String, Object>> taskList = taskMapper.selectMaps(queryWrapper);

    return groupTaskCountByDate(taskList);
  }

  /**
   * 获取最近一周的开始和结束时间
   *
   * @return
   */
  public String[] getTimeBoundary() {
    String[] times = new String[2];
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // 结束时间
    times[0] = dateFormat.format(new Date()) + " 23:59:59";
    // 获取一个星期前的日期
    calendar.add(Calendar.DATE, -7);
    Date lastWeekday = calendar.getTime();
    // 开始时间
    times[1] = dateFormat.format(lastWeekday) + " 00:00:00";
    return times;
  }

  /**
   * 根据日期对查询出最近一周的任务数量进行分组
   *
   * @param taskList
   * @return
   */
  Map<String, Long> groupTaskCountByDate(List<Map<String, Object>> taskList) {
    // 用于存放一周的任务数量
    Map<String, Long> taskCountList = new LinkedHashMap<>();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // 最近一周的日期集合
    List<Date> dateList = BlogUtil.getLatestWeek();
    for (Date date : dateList) {
      Map<String, Object> map =
          taskList.stream()
              .filter(r -> r.get("Day").equals(dateFormat.format(date)))
              .findAny()
              .orElse(null);
      if (map == null) {
        taskCountList.put(BlogUtil.getWeekOfDate(date), (long) 0);
      } else {
        taskCountList.put(BlogUtil.getWeekOfDate(date), (Long) map.get("Count"));
      }
    }
    return taskCountList;
  }

  /**
   * 根据日期对查询出最近一年的任务数量进行分组
   *
   * @param taskList
   * @return
   */
  Map<String, Long> groupTaskCountByMonth(List<Map<String, Object>> taskList) {
    // 最近12个月年月集合
    List<String> monthList = BlogUtil.getLatest12Month();
    // 用于存放一周的任务数量
    Map<String, Long> taskMap = new LinkedHashMap<>(16);

    for (String month : monthList) {
      Map<String, Object> map =
          taskList.stream().filter(r -> r.get("Month").equals(month)).findAny().orElse(null);
      if (map == null) {
        taskMap.put(month, (long) 0);
      } else {
        taskMap.put(month, (Long) map.get("Count"));
      }
    }
    return taskMap;
  }
}
