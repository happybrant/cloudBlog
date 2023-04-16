package com.kongfu.frontend.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/** @Author fuCong @Date 2023/2/9 16:19 展示在首页的基础数据 */
@Data
public class IndexData {

  /** 统计各分类下博客的数量 */
  private List<Category> categories;
  /** 统计各个月份下博客的数量 */
  private Map<String, Long> articleMap;
  /** 统计各个标签下博客的数量 */
  private Map<String, Long> tagMap;
  /** 近期发布的文章 */
  private List<Article> recentPosts;
  /** 博客设置 */
  private Setting setting;
  /** 发布的博客总数 */
  private int articleCount;
  /** 分类的总数 */
  private long categoryCount;
  /** 标签的总数 */
  private long tagCount;
}
