package com.framework.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类实体
 *
 * @author 付聪
 */
@NoArgsConstructor
@Data
@TableName(value = "ts_category")
public class Category extends MyBaseEntity {

  /** 父节点id */
  @TableField("parent_id")
  private String parentId;

  /** 分类名称 */
  @TableField("name")
  private String name;

  /** 完整的分类路径 */
  @TableField("path")
  private String path;

  /** 描述 */
  @TableField("description")
  private String description;

  /** 排序 */
  @TableField("`order`")
  private Integer order;

  @TableField(exist = false)
  /** 分类下博客的数量 */
  private int count;

  @TableField(exist = false)
  private List<Category> children;

  public Category(
      String id, String name, String parentId, String path, int count, List<Category> children) {
    this.setId(id);
    this.name = name;
    this.parentId = parentId;
    this.path = path;
    this.count = count;
    this.children = children;
  }
}
