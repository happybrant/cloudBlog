package com.kongfu.backend.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.NoteQuery;
import com.kongfu.backend.model.entity.Note;
import com.kongfu.backend.service.NoteService;
import com.kongfu.backend.util.BlogConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/** @author 付聪 */
@RequestMapping("/note")
@RestController
@RequiredArgsConstructor
public class NoteController implements BlogConstant {
  @Resource public NoteService noteService;

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
    if (map != null && map.size() > 0) {
      Object pageIndex = map.get("pageIndex");
      Object pageSize = map.get("pageSize");
      if (pageSize != null && pageIndex != null) {
        if (StringUtils.isNumber(pageIndex.toString())
            && StringUtils.isNumber(pageSize.toString())) {
          query.setPageSize((Integer) pageSize);
          query.setPageIndex((Integer) pageIndex);
        }
      }
      Object title = map.get("title");
      if (title != null) {
        query.setTitle(title.toString());
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
}
