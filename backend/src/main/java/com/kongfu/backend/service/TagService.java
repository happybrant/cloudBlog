package com.kongfu.backend.service;


import com.kongfu.backend.dao.TagMapper;
import com.kongfu.backend.entity.Tag;
import com.kongfu.backend.entity.TagArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    public TagMapper tagMapper;

    /**
     * 查找所有标签及其对应的博客数量
     * @return
     */
    public List<Tag> findTags(){
      return tagMapper.selectTagCategory();
    }

    /**
     * 获取所有的标签
     * @return
     */
    public List<Tag> findAllTags(int offset, int limit){
        return tagMapper.selectTags(offset, limit);
    }

    public int findTagCount(){
        return tagMapper.selectTagCount();
    }

    /**
     * 插入标签文章关联信息
     * @param tagArticles
     * @return
     */
    public int insertTagArticle(List<TagArticle> tagArticles){
        return  tagMapper.insertTagArticle(tagArticles);
    }

}
