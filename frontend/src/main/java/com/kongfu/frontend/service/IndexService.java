package com.kongfu.frontend.service;

import com.kongfu.frontend.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

  /**
   * 获取首页基础数据
   *
   * @return
   */
  public IndexData getIndexData(String router) {
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
    int articleCount = articleService.findArticleCount(new ArticleQuery(router));
    long categoryCount = categoryService.findCategoryCount(router);
    long tagCount = tagService.findTagCount(router);
    IndexData indexData = new IndexData();
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
    return indexData;
  }
}
