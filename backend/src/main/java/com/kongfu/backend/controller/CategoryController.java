package com.kongfu.backend.controller;

import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import com.kongfu.backend.model.dto.ArticleQuery;
import com.kongfu.backend.model.entity.Article;
import com.kongfu.backend.model.entity.Category;
import com.kongfu.backend.model.vo.ObjectRespBean;
import com.kongfu.backend.service.ArticleService;
import com.kongfu.backend.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 付聪
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    public CategoryService categoryService;
    @Resource
    public ArticleService articleService;

    /**
     * 分类列表
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<List<Category>> getCategoryList() {
        List<Category> categoryList = categoryService.getCategoryList();
        return new ResponseResult<>(ResponseResultCode.Success, "操作成功", categoryList);
    }

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping("/add")
    public ResponseResult<String> addCategory(@RequestBody Category category) {
        ResponseResult<String> result;
        if (category == null) {
            return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
        }
        int i = categoryService.addCategory(category);
        if (i > 0) {
            result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功添加" + i + "条数据");
        } else {
            result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
        }
        return result;
    }

    /**
     * 修改分类
     *
     * @param categories
     * @return
     */
    @PostMapping("/update")
    public ResponseResult<String> updateCategory(@RequestBody List<Category> categories) {
        ResponseResult<String> result;
        if (categories == null || categories.size() <= 0) {
            return new ResponseResult<>(ResponseResultCode.ParameterEmpty, "参数为空，操作失败");
        }
        int count = 0;
        for (Category category : categories) {
            count += categoryService.updateCategory(category);
        }
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
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseResult<String> deleteCategory(@RequestParam("id") int id) {
        ResponseResult<String> result;
        if (id == 0) {
            result = new ResponseResult<>(ResponseResultCode.ParameterEmpty, "传入参数为空");
        } else {
            ArticleQuery query = new ArticleQuery();
            query.setCategoryId(id);
            // 先判断该分类下是否有文章，如果有，则不允许删除
            List<Article> articleList = articleService.getArticleList(query);
            if (articleList.size() > 0) {
                return new ResponseResult<>(ResponseResultCode.Error, "当前分类下存在文章，无法删除");
            }
            int i = categoryService.deleteCategory(id);
            if (i > 0) {
                result = new ResponseResult<>(ResponseResultCode.Success, "操作成功", "成功删除" + i + "条数据");
            } else {
                result = new ResponseResult<>(ResponseResultCode.Error, "操作失败");
            }
        }
        return result;
    }

    /**
     * 根据父分类获取子分类
     *
     * @param rootCategory
     * @return
     */
    @RequestMapping("getChildrenCategories/{rootCategory}")
    @ResponseBody
    public ObjectRespBean getChildrenCategories(@PathVariable("rootCategory") String rootCategory) {
        return ObjectRespBean.returnSuccess(categoryService.findChildrenCategories(rootCategory));
    }

    /**
     * 移动分类
     *
     * @param type
     * @param name
     */
    @RequestMapping("move")
    @ResponseBody
    public ObjectRespBean move(@RequestParam("type") String type, @RequestParam("name") String name) {
        List<Category> siblingCategories = categoryService.findSiblingCategories(name);
        if (siblingCategories != null && siblingCategories.size() > 0) {
            for (int i = 0; i < siblingCategories.size(); i++) {
                Category category = siblingCategories.get(i);
                if (category.getName().equals(name)) {
                    if (type.equals("up")) {
                        // 获取上一个节点
                        if (i == 0) {
                            return ObjectRespBean.returnFail("首节点无法上移");
                        } else {
                            Category pre = siblingCategories.get(i - 1);
                            pre.setOrder(pre.getOrder() + 1);
                            category.setOrder(category.getOrder() - 1);
                            categoryService.updateCategory(category);
                            categoryService.updateCategory(pre);
                        }
                    } else {
                        if (i == siblingCategories.size() - 1) {
                            return ObjectRespBean.returnFail("尾节点无法下移");
                        } else {
                            Category next = siblingCategories.get(i + 1);
                            next.setOrder(next.getOrder() - 1);
                            category.setOrder(category.getOrder() + 1);
                            categoryService.updateCategory(category);
                            categoryService.updateCategory(next);
                        }
                    }
                    break;
                }
            }
        }
        return ObjectRespBean.returnSuccess();
    }

    //  /**
    //   * 新增分类
    //   *
    //   * @param category
    //   * @return
    //   */
    //  @RequestMapping("add")
    //  @ResponseBody
    //  public ObjectRespBean addCategory(Category category) {
    //    // 根据名称查找分类，用于判断分类是否存在
    //    String name = category.getName();
    //    Category exist = categoryService.findCategory(name);
    //    if (exist != null) {
    //      return ObjectRespBean.returnFail("当前名称的分类已存在");
    //    }
    //    // 查找父分类的路径
    //    int parentId = category.getParentId();
    //    Category parentCategory = categoryService.findCategoryById(parentId);
    //    String path = "";
    //    if (parentCategory == null) {
    //      path = "/" + category.getName();
    //    } else {
    //      path = parentCategory.getPath() + "/" + category.getName();
    //    }
    //    category.setPath(path);
    //    categoryService.addCategory(category);
    //    int id = category.getId();
    //    return ObjectRespBean.returnSuccess(id);
    //  }
}
