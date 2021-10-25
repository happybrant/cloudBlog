package com.kongfu.frontend.dao;

import com.kongfu.frontend.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 查找所有分类
     * @return
     */
    List<Category> selectCategory();
}
