package com.kongfu.backend.controller;

import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.entity.Tag;
import com.kongfu.backend.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author 付聪 */
@RestController
@RequestMapping("/tag")
public class TagController {
  @Resource public TagService tagService;

  /**
   * 标签列表
   *
   * @return
   */
  @GetMapping("/list")
  @Log(menu = "个人中心/博客设置", description = "获取标签列表")
  public ResponseResult<List<Tag>> getTagList() {
    List<Tag> tagList = tagService.getTagList();
    return new ResponseResult<>(ResponseResultCode.Success, "操作成功", tagList);
  }

  /**
   * 新增标签
   *
   * @param tag
   * @return
   */
  @PostMapping("/add")
  @Log(menu = "个人中心/博客设置", description = "新增标签")
  public ResponseResult<String> addTag(@RequestBody Tag tag) {
    ResponseResult<String> result;
    if (tag == null) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    List<Tag> tags = tagService.selectTagByName(tag.getName());
    if (tags != null && tags.size() > 0) {
      return new ResponseResult<>(ResponseResultCode.Error, "该标签已存在");
    }
    int i = tagService.addTag(tag);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }

  /**
   * 根据id删除标签
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(menu = "个人中心/博客设置", description = "根据id删除标签")
  public ResponseResult<String> deleteTag(@RequestParam("id") Integer id) {
    ResponseResult<String> result;
    if (id == null || id == 0) {
      return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
    }
    int i = tagService.deleteTag(id);
    if (i > 0) {
      result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
    } else {
      result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
    }
    return result;
  }
}
