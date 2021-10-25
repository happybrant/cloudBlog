package com.kongfu.frontend.service;

import com.kongfu.frontend.dao.CategoryMapper;
import com.kongfu.frontend.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    public CategoryMapper categoryMapper;

    /**
     * 查找分类
     * @return
     */
    public List<Category> findCategories(){
        List<Category> categories = categoryMapper.selectCategory();
        //查找根节点的分类
        List<Category> rootCategories = categories.stream().filter(category -> category.getParentId() == 0).sorted(Comparator.comparing(Category::getOrder)).collect(Collectors.toList());
        for(Category category:rootCategories){
            int id = category.getId();
            int count = 0;
            //查找父分类下的子分类
            List<Category> categories1 = categories.stream().filter(s -> s.getParentId() == id).sorted(Comparator.comparing(Category::getOrder)).collect(Collectors.toList());
            for(Category category1:categories1){
                count += category1.getCount();
            }
            category.setCount(count);
            category.setCategoryList(categories1);
        }
        return rootCategories;
    }

}
