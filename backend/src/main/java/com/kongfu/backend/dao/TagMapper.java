package com.kongfu.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kongfu.backend.model.entity.Tag;
import com.kongfu.backend.model.entity.TagArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** @author 付聪 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

  /**
   * 插入标签文章关联信息
   *
   * @param tagArticles
   * @return
   */
  int insertTagArticle(List<TagArticle> tagArticles);

  /**
   * 根据文章id删除标签关联关系
   *
   * @param articleId
   */
  void deleteTagArticleByArticleId(int articleId);

  /**
   * 根据标签id删除关联关系
   *
   * @param tagId
   */
  void deleteTagArticleByTagId(int tagId);
}
