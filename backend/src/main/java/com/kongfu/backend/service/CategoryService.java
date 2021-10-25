package com.kongfu.backend.service;

import com.kongfu.backend.dao.CategoryMapper;
import com.kongfu.backend.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class CategoryService {

    @Autowired
    public CategoryMapper categoryMapper;

    /**
     * 查找所有父分类
     * @return
     */
    public List<Category> findCategories(){
        List<Category> categories = categoryMapper.selectCategories();
        return categories;
    }

    /**
     * 根据分类名称查询分类详情
     * @param category
     * @return
     */
    public Category findCategory(String category){
        return categoryMapper.selectCategory(category);
    }

    /**
     * 根据分类ID查询分类详情
     * @param id
     * @return
     */
    public Category findCategoryById(int id){
        return categoryMapper.selectCategoryById(id);
    }

    /**
     * 根据父分类下查找所有下属子分类
     * @param category
     * @return
     */
    public List<Category> findChildrenCategories(String category){
        List<Category> categories = categoryMapper.selectChildrenCategories(category);
        return categories;
    }

    /**
     * 根据某个子分类查找所有的兄弟分类，包括自身
     * @param name
     * @return
     */
    public List<Category> findSiblingCategories(String name){
        return categoryMapper.selectSiblingCategories(name);
    }

    /**
     * 修改分类
     * @param category
     */
    public void updateCategory(Category category){
        //设置当前时间为修改时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        category.setLastUpdateTime(df.format(new Date()));
        categoryMapper.updateCategory(category);
    }

    /**
     * 新增分类
     * @param category
     */
    public void addCategory(Category category){
        //设置当前时间为修改时间和创建时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        category.setLastUpdateTime(df.format(new Date()));
        category.setCreateTime(df.format(new Date()));
        categoryMapper.insertCategory(category);
    }

}
