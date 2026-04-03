package com.framework.backend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接收前端博客查询条件
 *
 * @author 付聪
 */
@Data
@NoArgsConstructor
public class ArticleQuery extends BaseQuery {
  private String id;

  private String title;

  private String categoryId;

  private Integer status;
}
