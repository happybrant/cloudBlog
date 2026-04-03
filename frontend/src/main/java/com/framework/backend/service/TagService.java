package com.framework.backend.service;

import com.framework.backend.dao.TagMapper;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 付聪
 */
@Service
public class TagService {

  @Autowired public TagMapper tagMapper;

  /**
   * 查找所有标签及其对应的博客数量
   *
   * @return
   */
  public List<Map<String, Object>> findTags(String router) {
    return tagMapper.selectTagCategory(router);
  }

  /**
   * 获取标签的总数
   *
   * @return
   */
  public int findTagCount(String router) {
    return tagMapper.selectTagCount(router);
  }
}
