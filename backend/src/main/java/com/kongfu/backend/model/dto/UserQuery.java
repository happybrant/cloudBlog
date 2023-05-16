package com.kongfu.backend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接收前端用户查询条件
 *
 * @author 付聪
 */
@Data
@NoArgsConstructor
public class UserQuery {
  /** 支持用户名和展示名查询 */
  private String name;

  private Integer type;

  private Integer status;

  private Integer pageSize;

  private Integer pageIndex;
}
