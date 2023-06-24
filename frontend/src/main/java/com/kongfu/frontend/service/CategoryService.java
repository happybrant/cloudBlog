package com.kongfu.frontend.service;

import com.kongfu.frontend.dao.CategoryMapper;
import com.kongfu.frontend.entity.Category;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    // 获取所有非根节点的分类
    List<Category> childrenCategories =
        categories.stream()
            .filter(category -> category.getParentId() != 0)
            .sorted(Comparator.comparing(Category::getOrder))
            .collect(Collectors.toList());
    // 将所有的非根节点挂在根节点下面
    for (Category category : childrenCategories) {
      int parentId = category.getParentId();
      int count = category.getCount();
      String parentName = category.getPath().split("/")[1];
      Category root =
          rootCategories.stream().filter(s -> s.getId() == parentId).findAny().orElse(null);
      if (root == null) {
        // 如果根节点下原本没有数据导致没有查出来，手动创建根节点，并把当前分类加到该根节点的子分类中去
        List<Category> children = new ArrayList<>();
        children.add(category);
        rootCategories.add(
            new Category(parentId, parentName, 0, "/" + parentName, count, children));
      } else {
        // 否则直接加到根节点的子分类中去
        List<Category> children = root.getChildren();
        if (children == null) {
          children = new ArrayList<>();
        }
        children.add(category);
        root.setChildren(children);
        root.setCount(root.getCount() + count);
      }
    }
    return rootCategories;
  }

  /**
   * 获取分类的总数
   *
   * @return
   */
  public int findCategoryCount(List<Category> categories) {
    int count = 0;
    for (Category category : categories) {
      count++;
      List<Category> children = category.getChildren();
      if (children != null && children.size() > 0) {
        count += children.size();
      }
    }
    return count;
  }
}
