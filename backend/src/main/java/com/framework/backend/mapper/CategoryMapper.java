package com.framework.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.backend.model.dto.CategoryQuery;
import com.framework.backend.model.entity.Category;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 付聪
 */
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
