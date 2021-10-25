package com.kongfu.frontend.dao;

import com.kongfu.frontend.entity.Tag;
import com.kongfu.frontend.entity.TagArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {

    /**
     * 查找所有标签及其对应的博客数量
     * @return
     */
    List<Tag> selectTagCategory();

    /**
     * 查找所有标签
     * @return
     */
    List<Tag> selectTag();

    /**
     * 插入标签文章关联信息
     * @param tagArticles
     * @return
     */
    int insertTagArticle(List<TagArticle> tagArticles);
}
