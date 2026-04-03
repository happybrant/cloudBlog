package com.framework.backend.model.entity;

import lombok.Data;

/**
 * 标签博客实体
 *
 * @author 付聪
 */
@Data
public class TagArticle {

  private int id;
  private String tagId;
  private String articleId;

  public TagArticle(String tagId, String articleId) {
    this.tagId = tagId;
    this.articleId = articleId;
  }
}
