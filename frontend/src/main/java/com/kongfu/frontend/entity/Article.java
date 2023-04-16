package com.kongfu.frontend.entity;

import lombok.Data;

import java.util.List;

/**
 * 博客类实体
 *
 * @author 付聪
 */
@Data
public class Article extends Entity {

  private String title;
  private String content;
  private String description;
  private int categoryId;
  private String coverUrl;
  private int[] tagIds;
  private List<String> tagNames;
  /** 分类路径 */
  private String path;
  /** 前一篇 */
  private Article preArticle;
  /** 后一篇 */
  private Article nextArticle;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public int[] getTagIds() {
    return tagIds;
  }

  public void setTagIds(int[] tagIds) {
    this.tagIds = tagIds;
  }
}
