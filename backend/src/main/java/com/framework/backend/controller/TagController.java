package com.framework.backend.controller;

import com.framework.backend.annotation.Log;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.entity.Tag;
import com.framework.backend.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 付聪
 */
@RestController
@RequestMapping("/tag")
@ResponseResult
public class TagController {
  @Autowired public TagService tagService;

  /**
   * 标签列表
   *
   * @return
   */
  @GetMapping("/list")
  public List<Tag> getTagList() {
    return tagService.getTagList();
  }

  /**
   * 新增标签
   *
   * @param tag
   * @return
   */
  @PostMapping("/add")
  @Log(module = "个人中心/博客设置", value = "新增标签")
  public String addTag(@RequestBody Tag tag) {
    List<Tag> tags = tagService.selectTagByName(tag.getName());
    if (tags != null && !tags.isEmpty()) {
      throw new BusinessException("该标签已存在！");
    }
    tagService.addTag(tag);
    return tag.getId();
  }

  /**
   * 根据id删除标签
   *
   * @param id
   * @return
   */
  @DeleteMapping("/delete")
  @Log(module = "个人中心/博客设置", value = "根据id删除标签")
  public void deleteTag(@RequestParam("id") String id) {
    tagService.deleteTag(id);
  }
}
