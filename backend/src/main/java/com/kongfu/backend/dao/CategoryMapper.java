package com.kongfu.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongfu.backend.model.dto.CategoryQuery;
import com.kongfu.backend.model.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** @author 付聪 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

  /**
   * 根据某个子分类查找所有的兄弟分类，包括自身
   *
   * @param query
   * @return
   */
  List<Category> selectSiblingCategories(CategoryQuery query);
}
