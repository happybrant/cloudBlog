package com.framework.backend.event;

import com.framework.backend.model.dto.ArticleDto;
import com.framework.backend.model.entity.Article;
import com.framework.backend.service.ArticleService;
import com.framework.backend.service.ElasticSearchService;
import com.framework.backend.util.BlogConstant;
import com.framework.backend.util.JacksonUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author fuCong @Date 2023/1/8 19:18
 */
@Component
@RabbitListener(queues = BlogConstant.ARTICLE_INDEX)
@Slf4j
public class ArticleReceiver {

  @Autowired private ElasticSearchService elasticSearchService;
  @Autowired private ArticleService articleService;

  @RabbitHandler
  public void process(Map<String, Object> message) {
    String type = (String) message.get("type");
    String articleId = (String) message.get("articleId");
    log.info("ArticleReceiver消费者收到" + type + "类型id：" + articleId);
    if ("delete".equals(type)) {
      elasticSearchService.delete(BlogConstant.ARTICLE_INDEX, articleId);
      return;
    }
    // 根据id找出对应的笔记
    Article article = articleService.getArticleById(articleId);
    String content = article.getContent();
    // 处理博客内容，提取纯文本
    // 去除<html>的标签
    content = content.replaceAll("</?[^>]+>", "");
    // 去除字符串中的空格,回车,换行符,制表符
    content = content.replaceAll("\\s*|\t|\r|\n", "");
    ArticleDto articleDto =
        new ArticleDto(article.getId(), article.getTitle(), content, article.getCreateUser());
    if (!elasticSearchService.existIndex(BlogConstant.ARTICLE_INDEX)) {
      elasticSearchService.createIndex(
          BlogConstant.ARTICLE_INDEX, BlogConstant.ARTICLE_MAPPING_SOURCE);
    }
    if ("insert".equals(type)) {
      elasticSearchService.insert(
          BlogConstant.ARTICLE_INDEX, JacksonUtil.toJson(articleDto), articleId);
    } else if ("update".equals(type)) {
      elasticSearchService.update(
          BlogConstant.ARTICLE_INDEX, JacksonUtil.toJson(articleDto), articleId);
    }
  }
}
