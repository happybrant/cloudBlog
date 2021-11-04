package com.kongfu.backend.service;


import com.kongfu.backend.dao.TagMapper;
import com.kongfu.backend.entity.Category;
import com.kongfu.backend.entity.ObjectRespBean;
import com.kongfu.backend.entity.Tag;
import com.kongfu.backend.entity.TagArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     * 根据标签名称查询标签详情
     * @param tagName
     * @return
     */
    public Tag findTag(String tagName){
        return tagMapper.selectTag(tagName);
    }

    /**
     * 插入标签文章关联信息
     * @param tagArticles
     * @return
     */
    public int insertTagArticle(List<TagArticle> tagArticles){
        return  tagMapper.insertTagArticle(tagArticles);
    }

    /**
     * 新增标签
     * @param tag
     */
    public void addTag(Tag tag){
        //设置当前时间为修改时间和创建时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tag.setLastUpdateTime(df.format(new Date()));
        tag.setCreateTime(df.format(new Date()));
        tagMapper.insertTag(tag);
    }

    /**
     * 删除标签
     * @param ids
     */
    public void deleteTags(int[] ids){
        tagMapper.batchDeleteTags(ids);
    }

    /**
     * 根据id获取标签
     * @param id
     * @return
     */
    public Tag findTagById(int id){
        return tagMapper.selectTagById(id);
    }

    /**
     * 修改标签
     * @param tag
     */
    public void updateTag(Tag tag){
        //设置当前时间为修改时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tag.setLastUpdateTime(df.format(new Date()));
        tagMapper.updateTag(tag);
    }

    /**
     *
     * @param id
     * @param direction
     * @return
     */
    public ObjectRespBean moveOrder(int id, String direction){
        String MOVE_UP =  "up";
        String MOVE_DOWN =  "down";
        //根据id获取当前tag
        Tag tag = tagMapper.selectTagById(id);
        //当前tag的order
        int currentOrder = 0;
        if(tag != null){
            currentOrder = tag.getOrder();
        }
        int currentIndex = tagMapper.selectCurrentIndex(currentOrder);
        if(MOVE_UP.equals(direction)){
            //上移
            if(currentIndex <= 1){
                return ObjectRespBean.returnFail("首节点无法上移");
            }
            else{
                //根据当前排序获取该条数据的上一条数据
                Tag preTag = tagMapper.selectPreTag(currentOrder);
                if(preTag != null){
                    //交换两条数据的order
                    tag.setOrder(preTag.getOrder());
                    preTag.setOrder(currentOrder);
                }
                tagMapper.updateTag(tag);
                tagMapper.updateTag(preTag);
            }
        }
        else if(MOVE_DOWN.equals(direction)){
            //下移
            if(currentIndex == tagMapper.selectTagCount()){
                return ObjectRespBean.returnFail("尾节点无法下移");
            }
            else{
                //根据当前排序获取该条数据的下一条数据
                Tag nextTag = tagMapper.selectNextTag(currentOrder);
                if(nextTag != null){
                    //交换两条数据的order
                    tag.setOrder(nextTag.getOrder());
                    nextTag.setOrder(currentOrder);
                }
                tagMapper.updateTag(tag);
                tagMapper.updateTag(nextTag);
            }
        }
        else{
            return  ObjectRespBean.returnFail("找不到上移的方向");
        }
        return  ObjectRespBean.returnSuccess();
    }

}
