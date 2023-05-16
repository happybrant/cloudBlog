package com.kongfu.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 接收前端日志查询条件 @Author fuCong @Date 2023/2/8 17:18 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessLogQuery {

  /** 关键词，支持菜单和描述查询 */
  private String keyword;

  /** 请求时间范围之开始时间 */
  private String startTime;
  /** 请求时间范围之结束时间 */
  private String endTime;

  /** 接口耗时 */
  private long totalMillis;

  private Integer pageSize;

  private Integer pageIndex;

  private Integer startRow;
}
