package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.dao.ArticleMapper;
import com.kongfu.backend.model.dto.ArticleQuery;
import com.kongfu.backend.model.entity.Article;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.model.vo.LoginToken;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.BlogUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** @author 付聪 */
@Service
public class ArticleService {

  @Resource public ArticleMapper articleMapper;
  @Resource public HostHolder holder;
  @Resource private RestHighLevelClient client;
  /**
   * 博客列表，支持分页和条件查询
   *
   * @return
   */
  public Page<Article> getArticleListPager(ArticleQuery query) {
    Page<Article> articlePage = new Page<>(query.getPageIndex(), query.getPageSize());
    List<Article> articleList = articleMapper.selectArticleList(query);
    int total = articleMapper.selectArticleCount(query);
    articlePage.setRecords(articleList);
    articlePage.setTotal(total);
    return articlePage;
  }

  /**
   * 博客列表，支持条件查询
   *
   * @param query
   * @return
   */
  public List<Article> getArticleList(ArticleQuery query) {
    return articleMapper.selectArticleList(query);
  }

  /**
   * 根据博客id获取博客
   *
   * @param id
   * @return
   */
  public Article getArticleById(int id) {
    return articleMapper.selectArticleById(id);
  }
  /**
   * 获取当前用户的关于我文档
   *
   * @return
   */
  public Article getAboutMeArticle() {
    LoginToken user = holder.getUser();
    int currentUserId = 0;
    if (user != null) {
      currentUserId = user.getId();
    }
    QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("create_user", currentUserId);
    queryWrapper.and(
        wrapper ->
            wrapper
                .eq("status", BlogConstant.ABOUT_ME_STATUS)
                .or()
                .eq("status", BlogConstant.ABOUT_ME_UN_PUBLISH_STATUS));

    return articleMapper.selectOne(queryWrapper);
  }
  /**
   * 新增博客
   *
   * @param article
   * @return
   */
  public int addArticle(Article article) {
    return articleMapper.insert(article);
  }

  /**
   * 根据id更新博客
   *
   * @param article
   * @return
   */
  public int updateArticle(Article article) {
    return articleMapper.updateById(article);
  }

  /**
   * 根据id删除文章,逻辑删除
   *
   * @param ids
   * @return
   */
  public int deleteArticle(List<Integer> ids) {
    int count = 0;
    for (int id : ids) {
      Article article = articleMapper.selectById(id);
      if (article != null) {
        // 将状态修改为0
        article.setStatus(BlogConstant.DELETE_STATUS);
        count += articleMapper.updateById(article);
      }
    }
    return count;
  }

  /**
   * 获取博客列表
   *
   * @return
   */
  public List<Article> getArticleList() {
    QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", BlogConstant.DELETE_STATUS);
    return articleMapper.selectList(queryWrapper);
  }

  /**
   * 根据创建时间的月份对博客进行分组
   *
   * @return
   */
  public Map<String, Long> getArticleByMonth() {
    QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", BlogConstant.DELETE_STATUS);
    queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m')");
    queryWrapper.select(" DATE_FORMAT(create_time,'%Y-%m') AS Month,COUNT(*) AS Count");
    List<Map<String, Object>> noteList = articleMapper.selectMaps(queryWrapper);

    List<String> monthList = BlogUtil.getLatest12Month();
    Map<String, Long> articleMap = new LinkedHashMap<>(16);
    for (String month : monthList) {
      Map<String, Object> map =
          noteList.stream().filter(r -> r.get("Month").equals(month)).findAny().orElse(null);
      if (map == null) {
        articleMap.put(month, (long) 0);
      } else {
        articleMap.put(month, (Long) map.get("Count"));
      }
    }
    return articleMap;
  }

  /**
   * 根据分类对博客进行分组
   *
   * @return
   */
  public Map<String, Long> getArticleByCategory() {
    List<Map<String, Object>> articleList = articleMapper.selectArticleByCategory();
    Map<String, Long> articleMap = new LinkedHashMap<>(16);
    for (Map<String, Object> map : articleList) {
      articleMap.put(map.get("CategoryName").toString(), (Long) map.get("Count"));
    }
    return articleMap;
  }

  /**
   * 根据标签对博客进行分组
   *
   * @return
   */
  public Map<String, Long> getArticleByTag() {
    List<Map<String, Object>> articleList = articleMapper.selectArticleByTag();
    QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("status", BlogConstant.DELETE_STATUS);
    Long total = articleMapper.selectCount(queryWrapper);

    Map<String, Long> articleMap = new LinkedHashMap<>(16);
    if (total > 0) {
      for (Map<String, Object> map : articleList) {
        articleMap.put(map.get("TagName").toString(), (Long) map.get("Count") * 100 / total);
      }
    }
    return articleMap;
  }
}
