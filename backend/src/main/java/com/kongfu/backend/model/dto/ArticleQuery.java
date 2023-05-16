package com.kongfu.backend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接收前端博客查询条件
 *
 * @author 付聪
 */
@Data
@NoArgsConstructor
public class ArticleQuery {

  private String title;

  private Integer categoryId;

  private Integer status;

  private String createTime;

  private Integer pageSize;

  private Integer pageIndex;

  private Integer startRow;
}
