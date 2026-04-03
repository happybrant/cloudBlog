package com.framework.backend.service;

import com.framework.backend.dao.CategoryMapper;
import com.framework.backend.entity.Category;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 付聪
 */
@Service
public class CategoryService {

  @Autowired public CategoryMapper categoryMapper;

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
            .filter(category -> StringUtils.isBlank(category.getParentId()))
            .sorted(Comparator.comparing(Category::getOrder))
            .collect(Collectors.toList());
    // 获取所有非根节点的分类
    List<Category> childrenCategories =
        categories.stream()
            .filter(category -> StringUtils.isNotBlank(category.getParentId()))
            .sorted(Comparator.comparing(Category::getOrder))
            .toList();
    // 将所有的非根节点挂在根节点下面
    for (Category category : childrenCategories) {
      String parentId = category.getParentId();
      int count = category.getCount();
      String parentName = category.getPath().split("/")[1];
      Category root =
          rootCategories.stream()
              .filter(s -> Objects.equals(s.getId(), parentId))
              .findAny()
              .orElse(null);
      if (root == null) {
        // 如果根节点下原本没有数据导致没有查出来，手动创建根节点，并把当前分类加到该根节点的子分类中去
        List<Category> children = new ArrayList<>();
        children.add(category);
        rootCategories.add(
            new Category(parentId, parentName, "", "/" + parentName, count, children));
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
      if (children != null && !children.isEmpty()) {
        count += children.size();
      }
    }
    return count;
  }
}
