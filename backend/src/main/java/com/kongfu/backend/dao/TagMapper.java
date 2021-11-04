package com.kongfu.backend.dao;

import com.kongfu.backend.entity.Category;
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

    /**
     * 根据标签名称获取标签详情
     * @param tagName
     * @return
     */
    Tag selectTag(String tagName);

    /**
     * 插入一条标签数据
     * @param tag
     */
    void insertTag(Tag tag);

    /**
     * 批量删除标签
     * @param ids
     */
    void batchDeleteTags(int[] ids);

    /**
     * 根据id获取标签详情
     * @param id
     * @return
     */
    Tag selectTagById(int id);

    /**
     * 修改标签详情
     * @param tag
     */
    void updateTag(Tag tag);

    /**
     * 计算当前order的数据是第几个有效数据
     * @param order
     * @return
     */
    int selectCurrentIndex(int order);

    /**
     * 根据order排序后，获取上一条数据
     * @param order
     * @return
     */
    Tag selectPreTag(int order);
    /**
     * 根据order排序后，获取下一条数据
     * @param order
     * @return
     */
    Tag selectNextTag(int order);
}
