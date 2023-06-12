package com.kongfu.frontend.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @className: StatisticIndexEnum
 * @description: TODO
 * @author: Garen-chen
 * @date: 2023/5/30
 */
public enum StatisticIndexEnum {

  /** 各个分类下博客的数量 */
  CATEGORIES("categories", "各个分类下博客的数量"),
  /** 各个月份下博客的数量 */
  ARTICLEMAP("articleMap", "各个月份下博客的数量"),
  /** 各个标签下博客的数量 */
  TAGMAP("tagMap", "各个标签下博客的数量"),
  /** 近期发布的文章 */
  RECENTPOSTS("recentPosts", "近期发布的文章"),
  /** 博客设置 */
  SETTING("setting", "博客设置"),
  /** 发布的博客总数 */
  ARTICLECOUNT("articleCount", "发布的博客总数"),
  /** 分类的总数 */
  CATEGORYCOUNT("categoryCount", "发布的博客总数"),
  /** 标签的总数 */
  TAGCOUNT("tagCount", "标签的总数");

  public String redisKey;

  public String value;

  StatisticIndexEnum(String redisKey, String value) {
    this.redisKey = redisKey;
    this.value = value;
  }

  /**
   * 获取所有的key
   *
   * @return
   */
  public static List<String> getRedisKeys() {
    List<String> keys = new ArrayList<>();
    for (StatisticIndexEnum statisticIndexEnum : StatisticIndexEnum.values()) {
      keys.add(statisticIndexEnum.redisKey);
    }
    return keys;
  }
}
