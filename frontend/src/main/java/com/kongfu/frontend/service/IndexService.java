package com.kongfu.frontend.service;

import com.kongfu.frontend.common.StatisticIndexEnum;
import com.kongfu.frontend.entity.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** @Author fuCong @Date 2023/2/9 16:06 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IndexService {
  @Resource ArticleService articleService;
  @Resource CategoryService categoryService;
  @Resource TagService tagService;
  @Resource SettingService settingService;
  @Resource private RedisTemplate<String, Serializable> redisTemplate;

  /**
   * 获取首页基础数据
   *
   * @return
   */
  public IndexData getIndexData(String router) {
    if (!judgeHomeSetNull(router)) {
      IndexData indexData = new IndexData();
      List<Category> categories =
          (List<Category>)
              redisTemplate
                  .opsForValue()
                  .get(router + "_" + StatisticIndexEnum.CATEGORIES.redisKey);
      Map<String, Long> articleMap =
          (Map<String, Long>)
              redisTemplate
                  .opsForValue()
                  .get(router + "_" + StatisticIndexEnum.ARTICLEMAP.redisKey);
      Map<String, Long> tagMap =
          (Map<String, Long>)
              redisTemplate.opsForValue().get(router + "_" + StatisticIndexEnum.TAGMAP.redisKey);
      List<Article> recentPosts =
          (List<Article>)
              redisTemplate
                  .opsForValue()
                  .get(router + "_" + StatisticIndexEnum.RECENTPOSTS.redisKey);
      Setting setting =
          (Setting)
              redisTemplate.opsForValue().get(router + "_" + StatisticIndexEnum.SETTING.redisKey);
      int articleCount =
          (int)
              redisTemplate
                  .opsForValue()
                  .get(router + "_" + StatisticIndexEnum.ARTICLECOUNT.redisKey);
      int categoryCount =
          (int)
              redisTemplate
                  .opsForValue()
                  .get(router + "_" + StatisticIndexEnum.CATEGORYCOUNT.redisKey);
      int tagCount =
          (int)
              redisTemplate.opsForValue().get(router + "_" + StatisticIndexEnum.TAGCOUNT.redisKey);
      indexData.setCategories(categories);
      indexData.setArticleMap(articleMap);
      indexData.setTagMap(tagMap);
      indexData.setRecentPosts(recentPosts);
      indexData.setSetting(setting);
      indexData.setArticleCount(articleCount);
      indexData.setCategoryCount(categoryCount);
      indexData.setTagCount(tagCount);
      return indexData;
    } else {
      IndexData indexData = calculateStatistic(router);
      Setting setting = calculateSetting(router);
      indexData.setSetting(setting);
      return indexData;
    }
  }

  /**
   * 计算博客相关的统计数据并放入redis中
   *
   * @param router
   * @return
   */
  public IndexData calculateStatistic(String router) {
    IndexData indexData = new IndexData();
    // 按月份归档
    List<Map<String, Object>> articles = articleService.findArticleGroupByMonth();
    // 分类归档
    List<Category> categories = categoryService.findCategories(router);
    // 标签归档
    List<Map<String, Object>> tags = tagService.findTags(router);
    // 获取近5篇博客
    ArticleQuery articleQuery = new ArticleQuery();
    articleQuery.setPageIndex(1);
    articleQuery.setPageSize(5);
    articleQuery.setRouter(router);
    List<Article> recentPosts = articleService.findArticleList(articleQuery);
    // 不把内容放入缓存
    recentPosts.forEach(item -> item.setContent(""));
    int articleCount = articleService.findArticleCount(new ArticleQuery(router));
    long categoryCount = categoryService.findCategoryCount(router);
    long tagCount = tagService.findTagCount(router);

    Map<String, Long> articleMap = new LinkedHashMap<>(16);
    for (Map<String, Object> map : articles) {
      String createTime = map.get("createTime").toString();
      articleMap.put(createTime, (Long) map.get("count"));
    }
    Map<String, Long> tagMap = new LinkedHashMap<>(16);
    for (Map<String, Object> map : tags) {
      tagMap.put(map.get("name").toString(), (Long) map.get("count"));
    }
    Setting setting = settingService.getSettingByCurrentUser(router);
    indexData.setSetting(setting);
    indexData.setArticleMap(articleMap);
    indexData.setArticleCount(articleCount);
    indexData.setCategories(categories);
    indexData.setCategoryCount(categoryCount);
    indexData.setTagMap(tagMap);
    indexData.setTagCount(tagCount);
    indexData.setRecentPosts(recentPosts);
    // 将计算得到的统计数据放入到缓存中
    setStatisticCache(indexData, router);
    return indexData;
  }

  /**
   * 获取设置信息并且放入redis中
   *
   * @param router
   * @return
   */
  public Setting calculateSetting(String router) {
    Setting setting = settingService.getSettingByCurrentUser(router);
    redisTemplate.opsForValue().set(router + "_" + StatisticIndexEnum.SETTING.redisKey, setting);
    return setting;
  }

  /**
   * 判断首页的redis热点数据是否为空
   *
   * @return
   */
  public boolean judgeHomeSetNull(String router) {
    for (String key : StatisticIndexEnum.getRedisKeys()) {
      if (redisTemplate.opsForValue().get(router + "_" + key) == null) {
        return true;
      }
    }
    return false;
  }

  public void setStatisticCache(IndexData indexData, String router) {
    // 设置缓存
    redisTemplate
        .opsForValue()
        .set(
            router + "_" + StatisticIndexEnum.CATEGORIES.redisKey,
            (Serializable) indexData.getCategories());
    redisTemplate
        .opsForValue()
        .set(
            router + "_" + StatisticIndexEnum.ARTICLEMAP.redisKey,
            (Serializable) indexData.getArticleMap());
    redisTemplate
        .opsForValue()
        .set(
            router + "_" + StatisticIndexEnum.TAGMAP.redisKey,
            (Serializable) indexData.getTagMap());
    redisTemplate
        .opsForValue()
        .set(
            router + "_" + StatisticIndexEnum.RECENTPOSTS.redisKey,
            (Serializable) indexData.getRecentPosts());
    redisTemplate
        .opsForValue()
        .set(
            router + "_" + StatisticIndexEnum.CATEGORYCOUNT.redisKey, indexData.getCategoryCount());
    redisTemplate
        .opsForValue()
        .set(router + "_" + StatisticIndexEnum.TAGCOUNT.redisKey, indexData.getTagCount());
    redisTemplate
        .opsForValue()
        .set(router + "_" + StatisticIndexEnum.ARTICLECOUNT.redisKey, indexData.getArticleCount());
  }
}
