package com.kongfu.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.BaseQuery;
import com.kongfu.backend.model.dto.NoteQuery;
import com.kongfu.backend.model.entity.Entity;
import com.kongfu.backend.model.entity.Note;
import com.kongfu.backend.model.entity.NoteCategory;
import com.kongfu.backend.service.NoteCategoryService;
import com.kongfu.backend.service.NoteService;
import com.kongfu.backend.util.BlogConstant;
import com.kongfu.backend.util.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/** @author 付聪 */
@RequestMapping("/note")
@RestController
@RequiredArgsConstructor
public class NoteController implements BlogConstant {
  @Resource public NoteService noteService;
  @Resource public NoteCategoryService noteCategoryService;
  /**
   * 笔记列表
   *
   * @return
   */
  @PostMapping("/list")
  @Log(menu = "笔记管理", description = "获取笔记列表")
  public ResponseResult<Page<Note>> getNoteList(@RequestBody Map<String, Object> map) {
    Page<Note> notePage = noteService.getNoteListPager(getNoteQuery(map));
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", notePage);
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
    if (map != null && map.size() > 0) {
      query.setPageIndex(MapUtil.getValueAsInteger(map, "pageIndex", 1));
      query.setPageSize(MapUtil.getValueAsInteger(map, "pageSize", 10));
      query.setTitle(MapUtil.getValueAsString(map, "title"));
      String code = MapUtil.getValueAsString(map, "code");
      // 根据code查找id
      QueryWrapper<NoteCategory> queryWrapper = new QueryWrapper<>();
      queryWrapper.lambda().eq(NoteCategory::getCode, code);
      queryWrapper.lambda().eq(Entity::getStatus, BlogConstant.PUBLISH_STATUS);
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
  @Log(menu = "笔记管理", description = "根据id获取笔记")
  public ResponseResult<Note> getNoteById(@RequestParam("id") int id) {
    if (id <= 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    Note note = noteService.getNoteById(id);
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", note);
  }

  /**
   * 新增笔记
   *
   * @param note
   * @return
   */
  @PostMapping("/add")
  @Log(menu = "笔记管理", description = "新增笔记")
  public ResponseResult<Object> addNote(@RequestBody Note note) {
    ResponseResult<Object> result;
    if (note == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = noteService.addNote(note);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", note.getId());
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 修改笔记
   *
   * @param note
   * @return
   */
  @PostMapping("/update")
  @Log(menu = "笔记管理", description = "修改笔记")
  public ResponseResult<String> updateNote(@RequestBody Note note) {
    ResponseResult<String> result;
    if (note == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = noteService.updateNote(note);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 根据id删除笔记
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(menu = "笔记管理", description = "删除笔记")
  public ResponseResult<String> deleteNote(@RequestParam("id") Integer id) {
    ResponseResult<String> result;
    if (id == null || id == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = noteService.deleteNote(id);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 笔记分类列表
   *
   * @return
   */
  @GetMapping("/category/list")
  @Log(menu = "个人中心/博客设置", description = "获取笔记分类列表")
  public ResponseResult<List<NoteCategory>> getNoteCategoryList() {
    List<NoteCategory> categoryList = noteCategoryService.getNoteCategoryList();
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", categoryList);
  }
  /**
   * 笔记分类分页列表
   *
   * @return
   */
  @PostMapping("/category/listPager")
  @Log(menu = "笔记管理", description = "获取笔记分类分页列表")
  public ResponseResult<Page<NoteCategory>> getNoteCategoryListPager(
      @RequestBody Map<String, Object> map) {
    BaseQuery query = new BaseQuery();
    query.setPageIndex(MapUtil.getValueAsInteger(map, "pageIndex", 1));
    query.setPageSize(MapUtil.getValueAsInteger(map, "pageSize", 10));
    query.setKeyword(MapUtil.getValueAsString(map, "keyword", ""));
    Page<NoteCategory> noteCategories = noteCategoryService.getNoteCategoryListPager(query);
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", noteCategories);
  }
  /**
   * 新增笔记分类
   *
   * @param category
   * @return
   */
  @PostMapping("/category/add")
  @Log(menu = "个人中心/博客设置", description = "新增笔记分类")
  public ResponseResult<String> addNoteCategory(@RequestBody NoteCategory category) {
    if (category == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int result = noteCategoryService.addNoteCategory(category);
    if (result > 0) {
      return new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功插入" + result + "条数据");
    } else {
      return new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
  }

  /**
   * 修改笔记分类
   *
   * @param categories
   * @return
   */
  @PostMapping("/category/update")
  @Log(menu = "个人中心/博客设置", description = "修改笔记分类")
  public ResponseResult<String> updateNoteCategory(@RequestBody List<NoteCategory> categories) {
    ResponseResult<String> result;
    if (categories == null || categories.size() == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int count = noteCategoryService.updateNoteCategory(categories);
    if (count > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功更新" + count + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 删除分类
   *
   * @param ids
   * @return
   */
  @DeleteMapping("/category/delete")
  @Log(menu = "个人中心/博客设置", description = "删除笔记分类")
  public ResponseResult<String> deleteNoteCategory(@RequestBody List<Integer> ids) {
    ResponseResult<String> result;
    if (ids == null || ids.size() == 0) {
      result = new ResponseResult<>(ResponseResultCode.ParameterEmpty, "传入参数为空");
    } else {
      int i = noteCategoryService.deleteNoteCategory(ids);
      if (i > 0) {
        result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
      } else {
        result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
      }
    }
    return result;
  }
}
