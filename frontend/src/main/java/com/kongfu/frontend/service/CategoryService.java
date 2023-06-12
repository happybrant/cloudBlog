package com.kongfu.frontend.service;

import com.kongfu.frontend.dao.CategoryMapper;
import com.kongfu.frontend.entity.Category;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/** @author 付聪 */
@Service
public class CategoryService {

  @Resource public CategoryMapper categoryMapper;

  /**
   * 查找分类
   *
   * @return
   */
  public List<Category> findCategories(String router) {
    List<Category> categories = categoryMapper.selectCategory(router);
    // 查找根节点的分类
    List<Category> rootCategories =
        categories.stream()
            .filter(category -> category.getParentId() == 0)
            .sorted(Comparator.comparing(Category::getOrder))
            .collect(Collectors.toList());
    for (Category category : rootCategories) {
      int id = category.getId();
      int count = category.getCount();
      // 查找父分类下的子分类
      List<Category> categories1 =
          categories.stream()
              .filter(s -> s.getParentId() == id)
              .sorted(Comparator.comparing(Category::getOrder))
              .collect(Collectors.toList());
      for (Category category1 : categories1) {
        count += category1.getCount();
      }
      category.setCount(count);
      category.setChildren(categories1);
    }
    return rootCategories;
  }

  /**
   * 获取分类的总数
   *
   * @return
   */
  public int findCategoryCount(String router) {
    return categoryMapper.selectCategoryCount(router);
  }
}
