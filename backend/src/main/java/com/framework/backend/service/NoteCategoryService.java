package com.framework.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.common.MyBaseEntity;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.mapper.NoteCategoryMapper;
import com.framework.backend.model.dto.BaseQuery;
import com.framework.backend.model.dto.NoteQuery;
import com.framework.backend.model.entity.Note;
import com.framework.backend.model.entity.NoteCategory;
import com.framework.backend.model.entity.User;
import com.framework.backend.util.BlogConstant;
import com.framework.backend.utils.SecurityUtils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-06-13 12:38:00
 */
@Service
public class NoteCategoryService extends ServiceImpl<NoteCategoryMapper, NoteCategory> {
  @Autowired private NoteCategoryMapper noteCategoryMapper;
  @Autowired private NoteService noteService;

  public void addNoteCategory(NoteCategory noteCategory) {
    // 判断code不能重复
    judgeCodeExist(noteCategory, true);
    noteCategory.setOrder(getMaxOrder());
    noteCategoryMapper.insert(noteCategory);
  }

  /**
   * 获取下一个order
   *
   * @return
   */
  public int getMaxOrder() {
    // 获取最大的order
    int order = 1;
    QueryWrapper<NoteCategory> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(NoteCategory::getStatus, BlogConstant.PUBLISH_STATUS);
    queryWrapper.lambda().orderByDesc(NoteCategory::getOrder);
    List<NoteCategory> list = noteCategoryMapper.selectList(queryWrapper);
    if (list != null && !list.isEmpty()) {
      order = list.get(0).getOrder() + 1;
    }
    return order;
  }

  /**
   * 根据id更新博客分类
   *
   * @param categories
   */
  public void updateNoteCategory(List<NoteCategory> categories) {
    // 表示是修改分类信息操作
    if (categories.size() == 1) {
      NoteCategory category = categories.get(0);
      // 判断code不能重复
      judgeCodeExist(category, false);
    }
    int count = 0;
    for (NoteCategory noteCategory : categories) {
      count += noteCategoryMapper.updateById(noteCategory);
    }
  }

  /**
   * 分类编码不能重复
   *
   * @param category
   * @param includeSelf
   */
  private void judgeCodeExist(NoteCategory category, boolean includeSelf) {
    QueryWrapper<NoteCategory> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(NoteCategory::getCode, category.getCode());
    queryWrapper.lambda().eq(NoteCategory::getStatus, BlogConstant.PUBLISH_STATUS);
    if (!includeSelf) {
      queryWrapper.lambda().ne(MyBaseEntity::getId, category.getId());
    }
    User user = SecurityUtils.getCurrentUser();
    queryWrapper.lambda().eq(MyBaseEntity::getCreateUser, user.getId());
    List<NoteCategory> categoryList = baseMapper.selectList(queryWrapper);
    if (categoryList != null && !categoryList.isEmpty()) {
      throw new BusinessException("分类编码不能重复");
    }
  }

  /**
   * 获取博客分类列表
   *
   * @return
   */
  public List<NoteCategory> getNoteCategoryList() {
    QueryWrapper<NoteCategory> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().orderByAsc(NoteCategory::getOrder);
    queryWrapper.lambda().eq(NoteCategory::getStatus, BlogConstant.PUBLISH_STATUS);
    return noteCategoryMapper.selectList(queryWrapper);
  }

  /**
   * 分页获取博客分类列表
   *
   * @param baseQuery
   * @return
   */
  public Page<NoteCategory> getNoteCategoryListPager(BaseQuery baseQuery) {
    QueryWrapper<NoteCategory> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().orderByAsc(NoteCategory::getOrder);
    Page<NoteCategory> noteCategoryPage =
        new Page<>(baseQuery.getPageIndex(), baseQuery.getPageSize());
    if (StringUtils.isNotBlank(baseQuery.getKeyword())) {
      queryWrapper
          .lambda()
          .like(NoteCategory::getName, baseQuery.getKeyword())
          .or()
          .like(NoteCategory::getCode, baseQuery.getKeyword());
    }
    queryWrapper.lambda().eq(NoteCategory::getStatus, BlogConstant.PUBLISH_STATUS);
    return noteCategoryMapper.selectPage(noteCategoryPage, queryWrapper);
  }

  /**
   * 根据id删除博客分类，逻辑删除
   *
   * @param ids
   */
  public void deleteNoteCategory(List<String> ids) {
    for (String id : ids) {
      NoteQuery query = new NoteQuery();
      query.setCategoryId(id);
      // 先判断该分类下是否有文章，如果有，则不允许删除
      List<Note> noteList = noteService.getNoteList(query);
      if (!noteList.isEmpty()) {
        throw new BusinessException("当前分类下存在笔记，无法删除");
      }
    }
    NoteCategory noteCategory = new NoteCategory();
    noteCategory.setStatus(BlogConstant.TASK_DELETE_STATUS);
    UpdateWrapper<NoteCategory> updateWrapper = new UpdateWrapper<>();
    updateWrapper.lambda().in(MyBaseEntity::getId, ids);
    noteCategoryMapper.update(noteCategory, updateWrapper);
  }
}
