package com.kongfu.frontend.service;

import com.kongfu.frontend.dao.TagMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** @author 付聪 */
@Service
public class TagService {

  @Resource public TagMapper tagMapper;

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
