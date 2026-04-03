package com.framework.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.backend.entity.Tag;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 付聪
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

  /**
   * 查找所有标签及其对应的博客数量
   *
   * @param router
   * @return
   */
  List<Map<String, Object>> selectTagCategory(String router);

  /**
   * 查找所有标签
   *
   * @param router
   * @return
   */
  int selectTagCount(String router);
}
