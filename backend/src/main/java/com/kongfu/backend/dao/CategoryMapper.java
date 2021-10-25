package com.kongfu.backend.dao;

import com.kongfu.backend.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 查找所有分类
     * @return
     */
    List<Category> selectCategories();

    /**
     * 根据分类名称查询分类详情
     * @param category
     * @return
     */
    Category selectCategory(String category);

    /**
     * 根据分类id查询分类详情
     * @param id
     * @return
     */
    Category selectCategoryById(int id);

    /**
     * 根据父分类查找子分类
     * @return
     */
    List<Category> selectChildrenCategories(String category);

    /**
     * 根据某个子分类查找所有的兄弟分类，包括自身
     * @param name
     * @return
     */
    List<Category> selectSiblingCategories(String name);

    /**
     * 修改分类
     * @param category
     */
    void updateCategory(Category category);

    /**
     * 新增分类
     * @param category
     */
    void insertCategory(Category category);
}
