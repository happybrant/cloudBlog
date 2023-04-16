package com.kongfu.backend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：xutengfei
 * @date ：Created in 2021/11/11 19:04
 * @description：
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
