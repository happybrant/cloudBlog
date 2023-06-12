package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.backend.dao.CategoryMapper;
import com.kongfu.backend.model.dto.CategoryQuery;
import com.kongfu.backend.model.entity.Category;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** @author 付聪 */
@Service
public class CategoryService {

  @Resource public CategoryMapper categoryMapper;
  @Resource public HostHolder holder;

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
    if (rootCategories.size() > 0) {
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
  public int addCategory(Category category) {
    category.setStatus(BlogConstant.PUBLISH_STATUS);
    return categoryMapper.insert(category);
  }

  /**
   * 根据id更新分类
   *
   * @param category
   * @return
   */
  public int updateCategory(Category category) {
    return categoryMapper.updateById(category);
  }

  /**
   * 根据id删除分类,逻辑删除
   *
   * @param id
   * @return
   */
  public int deleteCategory(int id) {
    Category category = categoryMapper.selectById(id);
    if (category != null) {
      // 将状态修改为0
      category.setStatus(BlogConstant.DELETE_STATUS);
      return categoryMapper.updateById(category);
    }
    return 0;
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
