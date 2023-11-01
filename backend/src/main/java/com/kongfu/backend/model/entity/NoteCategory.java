package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 笔记分类实体
 *
 * @author 付聪
 */
@Data
@TableName(value = "ts_note_category")
public class NoteCategory extends Entity {

  /** 分类名称 */
  @TableField("name")
  private String name;

  /** 分类编码 */
  @TableField("code")
  private String code;

  /** 排序 */
  @TableField("`order`")
  private Integer order;
  /** 描述 */
  @TableField("description")
  private String description;
}
