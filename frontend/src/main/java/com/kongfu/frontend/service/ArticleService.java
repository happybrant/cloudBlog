package com.kongfu.frontend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.frontend.dao.ArticleMapper;
import com.kongfu.frontend.entity.Article;
import com.kongfu.frontend.entity.ArticleDto;
import com.kongfu.frontend.entity.ArticleQuery;
import com.kongfu.frontend.util.BlogConstant;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** @author 付聪 */
@Service
public class ArticleService {

  @Resource public ArticleMapper articleMapper;
  @Resource private RestHighLevelClient client;

  /**
   * 根据条件分页查找博客
   *
   * @param articleQuery
   * @return
   */
  public Page<Article> findArticlePager(ArticleQuery articleQuery) {
    Page<Article> articlePage = new Page<>(articleQuery.getPageIndex(), articleQuery.getPageSize());
    // 获取博客列表
    List<Article> articleList = findArticleList(articleQuery);
    // 获取博客总数
    int total = findArticleCount(articleQuery);
    articlePage.setRecords(articleList);
    articlePage.setTotal(total);
    return articlePage;
  }
  /**
   * 根据条件查找博客列表
   *
   * @param articleQuery
   * @return
   */
  public List<Article> findArticleList(ArticleQuery articleQuery) {
    articleQuery.setStartRow(articleQuery.getPageSize() * (articleQuery.getPageIndex() - 1));
    return articleMapper.selectArticle(articleQuery);
  }
  /**
   * 查找博客总数
   *
   * @return
   */
  public int findArticleCount(ArticleQuery articleQuery) {
    return articleMapper.selectArticleCount(articleQuery);
  }

  public Article findArticleByRouter(String router) {
    return articleMapper.selectArticleByRouter(router);
  }

  public Article findArticleById(int id) {
    Article article = articleMapper.selectArticleById(id);
    if (article != null) {
      // 获取前一篇
      Article preArticle = articleMapper.selectPreArticleById(id);
      // 获取后一篇
      Article nextArticle = articleMapper.selectNextArticleById(id);
      article.setPreArticle(preArticle);
      article.setNextArticle(nextArticle);
    }
    return article;
  }

  /**
   * 按月份对博客进行归档
   *
   * @return
   */
  public List<Map<String, Object>> findArticleGroupByMonth() {
    return articleMapper.selectArticleGroupByMonth();
  }

  public List<ArticleDto> searchArticleListPager(String keyword) throws IOException {

    // 搜索请求对象
    SearchRequest searchRequest = new SearchRequest(BlogConstant.ARTICLE_INDEX);
    // 搜索源构建对象
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    // 搜索条件不为空，按照条件进行搜索
    if (!StringUtils.isEmpty(keyword)) {
      // 构建高亮查询
      HighlightBuilder highlightBuilder = new HighlightBuilder();
      // 多个高亮显示
      highlightBuilder.requireFieldMatch(true);
      highlightBuilder.field("title");
      highlightBuilder.field("content");
      highlightBuilder.preTags("<em>").postTags("</em>");
      searchSourceBuilder.highlighter(highlightBuilder);
      // 构建查询对象
      searchSourceBuilder.query(QueryBuilders.queryStringQuery(keyword));
    } else {
      // 如果搜索条件为空，则查询全部数据的前10条
      searchSourceBuilder.query(QueryBuilders.matchAllQuery());
      // 设置分页信息
      searchSourceBuilder.from(0);
      searchSourceBuilder.size(5);
    }
    searchRequest.source(searchSourceBuilder);
    // 执行搜索,向ES发起http请求
    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

    // 搜索结果
    SearchHits hits = searchResponse.getHits();

    // 得到匹配度高的文档
    SearchHit[] searchHits = hits.getHits();
    List<ArticleDto> articleList = new ArrayList<>();
    for (SearchHit searchHit : searchHits) {
      // 获取完整数据
      Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
      int id = (int) sourceAsMap.get("id");
      String titleValue = (String) sourceAsMap.get("title");
      String contentValue = (String) sourceAsMap.get("content");
      // 获取高亮数据
      Map<String, HighlightField> fields = searchHit.getHighlightFields();
      HighlightField title = null;
      HighlightField content = null;
      if (fields != null && fields.size() > 0) {
        title = fields.get("title");
        content = fields.get("content");
      }
      ArticleDto articleDto = new ArticleDto();
      if (title == null) {
        articleDto.setTitle(titleValue);
      } else {
        articleDto.setTitle(title.getFragments()[0].toString());
      }
      if (content == null) {
        articleDto.setContent(contentValue);
      } else {
        articleDto.setContent(content.getFragments()[0].toString());
      }
      articleDto.setId(id);
      articleList.add(articleDto);
    }
    return articleList;
  }
}
