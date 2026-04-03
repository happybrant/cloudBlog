package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.mapper.CategoryMapper;
import com.framework.backend.model.dto.CategoryQuery;
import com.framework.backend.model.entity.Category;
import com.framework.backend.util.BlogConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
  public List<Category> getCategoryList() {
    QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", 1);
    List<Category> categoryList = categoryMapper.selectList(queryWrapper);
    // 获取所有的一级分类
    List<Category> rootCategories =
        categoryList.stream().filter(s -> s.getParentId() == 0).collect(Collectors.toList());
    if (!rootCategories.isEmpty()) {
      for (Category category : rootCategories) {
        buildChildTree(category, categoryList);
      }
    }
    return rootCategories;
  }

  /** 递归，建立子树形结构 */
  private Category buildChildTree(Category pCategory, List<Category> originCategories) {
    List<Category> childCategories = new ArrayList<>();
    for (Category category : originCategories) {
      if (pCategory.getId().equals(category.getParentId())) {
        childCategories.add(buildChildTree(category, originCategories));
      }
    }
    pCategory.setChildren(childCategories);
    return pCategory;
  }

  /**
   * 新增分类
   *
   * @param category
   * @return
   */
  public void addCategory(Category category) {
    // 查找当前层级是否有重名分类
    QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("name", category.getName());
    queryWrapper.eq("parent_id", category.getParentId());
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    List<Category> categoryList = categoryMapper.selectList(queryWrapper);
    if (categoryList != null && !categoryList.isEmpty()) {
      throw new BusinessException("当前层级存在同名分类！");
    }
    categoryMapper.insert(category);
  }

  /**
   * 根据id更新分类
   *
   * @param category
   */
  public void updateCategory(Category category) {
    categoryMapper.updateById(category);
  }

  /**
   * 根据id删除分类,逻辑删除
   *
   * @param id
   */
  public void deleteCategory(String id) {
    Category category = categoryMapper.selectById(id);
    if (category != null) {
      // 将状态修改为0
      category.setStatus(BlogConstant.DELETE_STATUS);
      categoryMapper.updateById(category);
    }
  }

  /**
   * 根据某个子分类查找所有的兄弟分类，包括自身
   *
   * @param name
   * @return
   */
  public List<Category> findSiblingCategories(String name) {
    CategoryQuery categoryQuery = new CategoryQuery();
    categoryQuery.setName(name);
    return categoryMapper.selectSiblingCategories(categoryQuery);
  }
}
