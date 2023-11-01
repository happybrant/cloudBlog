package com.kongfu.backend.model.dto;

import lombok.Data;

/** @Author fuCong @Date 2023/5/19 16:49 基础查询条件 */
@Data
public class BaseQuery {
  private Integer pageSize;

  private Integer pageIndex;

  private Integer startRow;

  private Integer createUser;

  private String createTime;

  private String keyword;
}
