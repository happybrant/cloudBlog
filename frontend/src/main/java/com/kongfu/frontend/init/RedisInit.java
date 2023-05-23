package com.kongfu.frontend.init;

import com.kongfu.frontend.service.ArticleService;
import com.kongfu.frontend.service.CategoryService;
import com.kongfu.frontend.service.SettingService;
import com.kongfu.frontend.service.TagService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/** @Author fuCong @Date 2023/5/19 13:51 初始化redis缓存 */
@Component
public class RedisInit {
  @Resource ArticleService articleService;
  @Resource CategoryService categoryService;
  @Resource TagService tagService;
  @Resource SettingService settingService;

  @Bean
  public void initCache() {
    // 按月份归档
    CompletableFuture<List<Map<String, Object>>> articles =
        CompletableFuture.supplyAsync(() -> articleService.findArticleGroupByMonth());

    // 分类归档
    //    CompletableFuture<List<Category>> categories =
    //        CompletableFuture.supplyAsync(() -> categoryService.findCategories(router));
    //    // 标签归档
    //    List<Map<String, Object>> tags = tagService.findTags(router);
  }
}
