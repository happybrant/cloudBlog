package com.kongfu.frontend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongfu.frontend.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** @author 付聪 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

  /**
   * 查找所有分类
   *
   * @param router
   * @return
   */
  List<Category> selectCategory(String router);

  /**
   * 查找分类总数
   *
   * @param router
   * @return
   */
  int selectCategoryCount(String router);
}
