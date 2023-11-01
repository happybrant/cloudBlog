package com.kongfu.backend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口前端笔记查询条件
 *
 * @author 付聪
 */
@Data
@NoArgsConstructor
public class NoteQuery extends BaseQuery {

  private String title;
  private int categoryId;
  private String categoryCode;
}
