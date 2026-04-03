package com.framework.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.framework.backend.annotation.Log;
import com.framework.backend.common.MyPage;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.dto.BaseQuery;
import com.framework.backend.model.dto.NoteQuery;
import com.framework.backend.model.entity.Note;
import com.framework.backend.model.entity.NoteCategory;
import com.framework.backend.service.NoteCategoryService;
import com.framework.backend.service.NoteService;
import com.framework.backend.util.BlogConstant;
import com.framework.backend.util.MapUtil;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 付聪
 */
@RequestMapping("/note")
@RestController
@RequiredArgsConstructor
@ResponseResult
public class NoteController implements BlogConstant {
  @Autowired public NoteService noteService;
  @Autowired public NoteCategoryService noteCategoryService;

  /**
   * 笔记列表
   *
   * @return
   */
  @PostMapping("/list")
  public MyPage<Note> getNoteList(@RequestBody Map<String, Object> map) {
    return noteService.getNoteListPager(getNoteQuery(map));
  }

  /**
   * 接口查询条件
   *
   * @param map
   * @return
   */
  private NoteQuery getNoteQuery(Map<String, Object> map) {
    NoteQuery query = new NoteQuery();
    query.setPageIndex(0);
    query.setPageSize(Integer.MAX_VALUE);
    query.setTitle("");
    if (map != null && !map.isEmpty()) {
      query.setPageIndex(MapUtil.getValueAsInteger(map, "pageIndex", 1));
      query.setPageSize(MapUtil.getValueAsInteger(map, "pageSize", 10));
      query.setTitle(MapUtil.getValueAsString(map, "title"));
      String code = MapUtil.getValueAsString(map, "code");
      // 根据code查找id
      QueryWrapper<NoteCategory> queryWrapper = new QueryWrapper<>();
      queryWrapper.lambda().eq(NoteCategory::getCode, code);
      queryWrapper.lambda().eq(NoteCategory::getStatus, BlogConstant.PUBLISH_STATUS);
      NoteCategory noteCategory = noteCategoryService.getOne(queryWrapper);
      if (noteCategory != null) {
        query.setCategoryId(noteCategory.getId());
      }
    }
    return query;
  }

  /**
   * 根据id获取笔记
   *
   * @param id
   * @return
   */
  @GetMapping("/getNoteById")
  public Note getNoteById(@RequestParam("id") int id) {
    if (id <= 0) {
      throw new BusinessException("参数为空，操作失败");
    }
    return noteService.getNoteById(id);
  }

  /**
   * 新增笔记
   *
   * @param note
   * @return
   */
  @PostMapping("/add")
  @Log(module = "笔记管理", value = "新增笔记")
  public String addNote(@RequestBody Note note) {
    if (note == null) {
      throw new BusinessException("参数为空，操作失败");
    }
    noteService.addNote(note);
    return note.getId();
  }

  /**
   * 修改笔记
   *
   * @param note
   * @return
   */
  @PostMapping("/update")
  @Log(module = "笔记管理", value = "修改笔记")
  public void updateNote(@RequestBody Note note) {
    if (note == null) {
      throw new BusinessException("参数为空，操作失败");
    }
    noteService.updateNote(note);
  }

  /**
   * 根据id删除笔记
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(module = "笔记管理", value = "删除笔记")
  public void deleteNote(@RequestParam("id") String id) {
    noteService.deleteNote(id);
  }

  /**
   * 笔记分类列表
   *
   * @return
   */
  @GetMapping("/category/list")
  public List<NoteCategory> getNoteCategoryList() {
    return noteCategoryService.getNoteCategoryList();
  }

  /**
   * 笔记分类分页列表
   *
   * @return
   */
  @PostMapping("/category/listPager")
  public Page<NoteCategory> getNoteCategoryListPager(@RequestBody Map<String, Object> map) {
    BaseQuery query = new BaseQuery();
    query.setPageIndex(MapUtil.getValueAsInteger(map, "pageIndex", 1));
    query.setPageSize(MapUtil.getValueAsInteger(map, "pageSize", 10));
    query.setKeyword(MapUtil.getValueAsString(map, "keyword", ""));
    return noteCategoryService.getNoteCategoryListPager(query);
  }

  /**
   * 新增笔记分类
   *
   * @param category
   * @return
   */
  @PostMapping("/category/add")
  @Log(module = "个人中心/博客设置", value = "新增笔记分类")
  public String addNoteCategory(@RequestBody NoteCategory category) {
    if (category == null) {
      throw new BusinessException("参数为空，操作失败");
    }
    noteCategoryService.addNoteCategory(category);
    return category.getId();
  }

  /**
   * 修改笔记分类
   *
   * @param categories
   * @return
   */
  @PostMapping("/category/update")
  @Log(module = "个人中心/博客设置", value = "修改笔记分类")
  public void updateNoteCategory(@RequestBody List<NoteCategory> categories) {
    if (categories == null || categories.isEmpty()) {
      throw new BusinessException("参数为空，操作失败");
    }
    noteCategoryService.updateNoteCategory(categories);
  }

  /**
   * 删除分类
   *
   * @param ids
   * @return
   */
  @DeleteMapping("/category/delete")
  @Log(module = "个人中心/博客设置", value = "删除笔记分类")
  public void deleteNoteCategory(@RequestBody List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      throw new BusinessException("传入参数为空");
    }
    noteCategoryService.deleteNoteCategory(ids);
  }
}
