package com.kongfu.backend.controller;

import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.ArticleQuery;
import com.kongfu.backend.model.entity.Article;
import com.kongfu.backend.model.entity.Category;
import com.kongfu.backend.service.ArticleService;
import com.kongfu.backend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author 付聪 */
@RestController
@RequestMapping("/category")
public class CategoryController {
  @Resource public CategoryService categoryService;
  @Resource public ArticleService articleService;

  /**
   * 分类列表
   *
   * @return
   */
  @GetMapping("/list")
  @Log(menu = "个人中心/博客设置", description = "获取分类列表")
  public ResponseResult<List<Category>> getCategoryList() {
    List<Category> categoryList = categoryService.getCategoryList();
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", categoryList);
  }

  /**
   * 新增分类
   *
   * @param category
   * @return
   */
  @PostMapping("/add")
  @Log(menu = "个人中心/博客设置", description = "新增分类")
  public ResponseResult<String> addCategory(@RequestBody Category category) {
    if (category == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    return categoryService.addCategory(category);
  }

  /**
   * 修改分类
   *
   * @param categories
   * @return
   */
  @PostMapping("/update")
  @Log(menu = "个人中心/博客设置", description = "修改分类")
  public ResponseResult<String> updateCategory(@RequestBody List<Category> categories) {
    ResponseResult<String> result;
    if (categories == null || categories.size() <= 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int count = 0;
    for (Category category : categories) {
      count += categoryService.updateCategory(category);
    }
    if (count > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + count + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 删除分类
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(menu = "个人中心/博客设置", description = "删除分类")
  public ResponseResult<String> deleteCategory(@RequestParam("id") int id) {
    ResponseResult<String> result;
    if (id == 0) {
      result = new ResponseResult<>(ResponseResultCode.ParameterEmpty, "传入参数为空");
    } else {
      ArticleQuery query = new ArticleQuery();
      query.setCategoryId(id);
      // 先判断该分类下是否有文章，如果有，则不允许删除
      List<Article> articleList = articleService.getArticleList(query);
      if (articleList.size() > 0) {
        return new ResponseResult<>(ResponseResultCode.Error, "当前分类下存在文章，无法删除");
      }
      int i = categoryService.deleteCategory(id);
      if (i > 0) {
        result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
      } else {
        result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
      }
    }
    return result;
  }

  /**
   * 移动分类
   *
   * @param type
   * @param name
   */
  @RequestMapping("move")
  @ResponseBody
  @Log(menu = "个人中心/博客设置", description = "移动分类")
  public ResponseResult<String> move(
      @RequestParam("type") String type, @RequestParam("name") String name) {
    List<Category> siblingCategories = categoryService.findSiblingCategories(name);
    if (siblingCategories != null && siblingCategories.size() > 0) {
      for (int i = 0; i < siblingCategories.size(); i++) {
        Category category = siblingCategories.get(i);
        if (category.getName().equals(name)) {
          if ("up".equals(type)) {
            // 获取上一个节点
            if (i == 0) {
              return new ResponseResult<>(ResponseResultCode.Error, "首节点无法上移");
            } else {
              Category pre = siblingCategories.get(i - 1);
              pre.setOrder(pre.getOrder() + 1);
              category.setOrder(category.getOrder() - 1);
              categoryService.updateCategory(category);
              categoryService.updateCategory(pre);
            }
          } else {
            if (i == siblingCategories.size() - 1) {
              return new ResponseResult<>(ResponseResultCode.Error, "尾节点无法下移");
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
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功");
  }
}
