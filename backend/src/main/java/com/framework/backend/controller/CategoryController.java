package com.framework.backend.controller;

import com.framework.backend.annotation.Log;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.dto.ArticleQuery;
import com.framework.backend.model.entity.Article;
import com.framework.backend.model.entity.Category;
import com.framework.backend.service.ArticleService;
import com.framework.backend.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 付聪
 */
@RestController
@RequestMapping("/category")
@ResponseResult
public class CategoryController {
  @Autowired public CategoryService categoryService;
  @Autowired public ArticleService articleService;

  /**
   * 分类列表
   *
   * @return
   */
  @GetMapping("/list")
  public List<Category> getCategoryList() {
    return categoryService.getCategoryList();
  }

  /**
   * 新增分类
   *
   * @param category
   * @return
   */
  @PostMapping("/add")
  public String addCategory(@RequestBody Category category) {
    categoryService.addCategory(category);
    return category.getId();
  }

  /**
   * 修改分类
   *
   * @param categories
   * @return
   */
  @PostMapping("/update")
  @Log(module = "个人中心/博客设置", value = "修改分类")
  public void updateCategory(@RequestBody List<Category> categories) {
    for (Category category : categories) {
      categoryService.updateCategory(category);
    }
  }

  /**
   * 删除分类
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(module = "个人中心/博客设置", value = "删除分类")
  public void deleteCategory(@RequestParam("id") String id) {
    ArticleQuery query = new ArticleQuery();
    query.setCategoryId(id);
    // 先判断该分类下是否有文章，如果有，则不允许删除
    List<Article> articleList = articleService.getArticleList(query);
    if (!articleList.isEmpty()) {
      throw new BusinessException("当前分类下存在文章，无法删除！");
    }
    categoryService.deleteCategory(id);
  }

  /**
   * 移动分类
   *
   * @param type
   * @param name
   */
  @ResponseBody
  @Log(module = "个人中心/博客设置", value = "移动分类")
  public void move(@RequestParam("type") String type, @RequestParam("name") String name) {
    List<Category> siblingCategories = categoryService.findSiblingCategories(name);
    if (siblingCategories != null && !siblingCategories.isEmpty()) {
      for (int i = 0; i < siblingCategories.size(); i++) {
        Category category = siblingCategories.get(i);
        if (category.getName().equals(name)) {
          if ("up".equals(type)) {
            // 获取上一个节点
            if (i == 0) {
              throw new BusinessException("首节点无法上移");
            } else {
              Category pre = siblingCategories.get(i - 1);
              pre.setOrder(pre.getOrder() + 1);
              category.setOrder(category.getOrder() - 1);
              categoryService.updateCategory(category);
              categoryService.updateCategory(pre);
            }
          } else {
            if (i == siblingCategories.size() - 1) {
              throw new BusinessException("尾节点无法下移");
            } else {
              Category next = siblingCategories.get(i + 1);
              next.setOrder(next.getOrder() - 1);
              category.setOrder(category.getOrder() + 1);
              categoryService.updateCategory(category);
              categoryService.updateCategory(next);
            }
          }
          break;
        }
      }
    }
  }
}
