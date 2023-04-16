package com.kongfu.frontend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/** @author 付聪 文章查询条件 */
@Data
@NoArgsConstructor
public class ArticleQuery {

  /** 分类名称 */
  private String category;
  /** 创建年份 */
  private Integer createYear;
  /** 创建月份 */
  private Integer createMonth;
  /** 标签名称 */
  private String tag;
  /** 每页数据条数 */
  private Integer pageSize;
  /** 路由 */
  private String router;
  /** 当前页码 */
  private Integer pageIndex;

  private Integer startRow;

  public ArticleQuery(String router) {
    this.router = router;
  }
}
