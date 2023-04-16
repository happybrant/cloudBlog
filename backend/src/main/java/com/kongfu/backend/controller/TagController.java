package com.kongfu.backend.controller;

import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.entity.Tag;
import com.kongfu.backend.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 付聪
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Resource
    public TagService tagService;

    /**
     * 分类列表
     *
     * @return
     */
    @GetMapping("/list")
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
    //  /**
    //   * 标签页面
    //   *
    //   * @return
    //   */
    //  @RequestMapping("/list")
    //  public String List() {
    //    return "/site/tag";
    //  }
    //
    //  @RequestMapping("/{id}")
    //  @ResponseBody
    //  public ObjectRespBean getTagById(@PathVariable("id") int id) {
    //    return ObjectRespBean.returnSuccess(tagService.findTagById(id));
    //  }
    //
    //  /**
    //   * 获取所有标签
    //   *
    //   * @return
    //   */
    //  @RequestMapping("getTags")
    //  @ResponseBody
    //  public PageModel getTags(HttpServletRequest request) {
    //    int offset = Integer.parseInt(request.getParameter("offset"));
    //    int limit = Integer.parseInt(request.getParameter("limit"));
    //    List<Tag> tags = tagService.findAllTags(offset, limit);
    //    // 标签总数
    //    int count = tagService.findTagCount();
    //    PageModel pageModel = new PageModel();
    //    pageModel.setRows(tags);
    //    pageModel.setTotal(count);
    //    return pageModel;
    //  }
    //
    //  /**
    //   * 新增标签
    //   *
    //   * @param tag
    //   * @return
    //   */
    //  @RequestMapping("add")
    //  @ResponseBody
    //  public ObjectRespBean addTag(Tag tag) {
    //    // 根据名称查找分类，用于判断分类是否存在
    //    String name = tag.getName();
    //    Tag exist = tagService.findTag(name);
    //    if (tag.getId() == null) {
    //      if (exist != null) {
    //        return ObjectRespBean.returnFail("当前名称的标签已存在");
    //      }
    //      // 新增标签
    //      tagService.addTag(tag);
    //    } else {
    //      if (!exist.getId().equals(tag.getId())) {
    //        // 如果当前的名字和其他标签重复提示用户
    //        return ObjectRespBean.returnFail("当前名称的标签已存在");
    //      } else {
    //        // 更新标签
    //        tagService.updateTag(tag);
    //      }
    //    }
    //    int id = tag.getId();
    //    return ObjectRespBean.returnSuccess(id);
    //  }
    //
    //  /**
    //   * 删除标签
    //   *
    //   * @param ids
    //   * @return
    //   */
    //  @RequestMapping("delete")
    //  @ResponseBody
    //  public ObjectRespBean deleteTags(@RequestBody int[] ids) {
    //    tagService.deleteTags(ids);
    //    return ObjectRespBean.returnSuccess();
    //  }
    //
    //  /**
    //   * 修改标签状态
    //   *
    //   * @param tag
    //   * @return
    //   */
    //  @RequestMapping("changeState")
    //  @ResponseBody
    //  public ObjectRespBean changeState(Tag tag) {
    //    tagService.updateTag(tag);
    //    return ObjectRespBean.returnSuccess();
    //  }
    //
    //  /**
    //   * 上下移动顺序
    //   *
    //   * @param map
    //   * @return
    //   */
    //  @RequestMapping("move")
    //  @ResponseBody
    //  public ObjectRespBean moveOrder(@RequestBody Map<String, String> map) {
    //    int id = Integer.parseInt(map.get("id"));
    //    String direction = map.get("direction");
    //    return tagService.moveOrder(id, direction);
    //  }
}
