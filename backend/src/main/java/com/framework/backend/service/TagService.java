package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.mapper.TagMapper;
import com.framework.backend.model.entity.Tag;
import com.framework.backend.model.entity.TagArticle;
import com.framework.backend.util.BlogConstant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 付聪
 */
@Service
public class TagService {

  @Autowired public TagMapper tagMapper;

  /**
   * 查找标签
   *
   * @return
   */
  public List<Tag> getTagList() {
    QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("status", BlogConstant.PUBLISH_STATUS);
    queryWrapper.orderByAsc("create_time");
    return tagMapper.selectList(queryWrapper);
  }

  /**
   * 新增标签
   *
   * @param tag
   * @return
   */
  public void addTag(Tag tag) {
    tagMapper.insert(tag);
  }

  /**
   * 根据名字查找标签
   *
   * @param name
   * @return
   */
  public List<Tag> selectTagByName(String name) {
    QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("name", name);
    return tagMapper.selectList(queryWrapper);
  }

  /**
   * 根据id删除标签,逻辑删除
   *
   * @param id
   */
  public void deleteTag(String id) {
    Tag tag = tagMapper.selectById(id);
    if (tag != null) {
      // 将状态修改为0
      tag.setStatus(BlogConstant.DELETE_STATUS);
      // 删除博客标签关联关系
      tagMapper.deleteTagArticleByTagId(id);
      tagMapper.updateById(tag);
    }
  }

  /**
   * 插入标签文章关联信息
   *
   * @param tagArticles
   */
  public void insertTagArticle(List<TagArticle> tagArticles) {
    tagMapper.insertTagArticle(tagArticles);
  }

  /**
   * 根据博客id删除标签文章关联信息
   *
   * @param articleId
   */
  public void deleteTagArticleByArticleId(String articleId) {
    tagMapper.deleteTagArticleByArticleId(articleId);
  }
}
