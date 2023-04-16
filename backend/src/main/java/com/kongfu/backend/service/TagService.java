package com.kongfu.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kongfu.backend.dao.TagMapper;
import com.kongfu.backend.model.entity.Tag;
import com.kongfu.backend.model.entity.TagArticle;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.util.BlogConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** @author 付聪 */
@Service
public class TagService {

  @Resource public TagMapper tagMapper;
  @Resource public HostHolder holder;

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
  public int addTag(Tag tag) {
    tag.setStatus(BlogConstant.PUBLISH_STATUS);
    return tagMapper.insert(tag);
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
   * @return
   */
  public int deleteTag(int id) {
    Tag tag = tagMapper.selectById(id);
    if (tag != null) {
      // 将状态修改为0
      tag.setStatus(BlogConstant.DELETE_STATUS);
      return tagMapper.updateById(tag);
    }
    return 0;
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
   * 删除标签文章关联信息
   *
   * @param articleId
   */
  public void deleteTagArticle(int articleId) {
    tagMapper.deleteTagArticle(articleId);
  }
}
