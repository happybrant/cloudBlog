package com.kongfu.backend.dao;

import com.kongfu.backend.entity.Tag;
import com.kongfu.backend.entity.TagArticle;
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
     * 分页查找标签
     * @param offset
     * @param limit
     * @return
     */
    List<Tag> selectTags(int offset, int limit);

    /**
     * 查找所有标签的数量
     * @return
     */
    int selectTagCount();

    /**
     * 插入标签文章关联信息
     * @param tagArticles
     * @return
     */
    int insertTagArticle(List<TagArticle> tagArticles);
}
