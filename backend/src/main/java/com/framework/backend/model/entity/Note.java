package com.framework.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 笔记类实体
 *
 * @author 付聪
 */
@Data
@NoArgsConstructor
@TableName(value = "ts_note")
public class Note extends MyBaseEntity {

  /** 标题 */
  @TableField("title")
  private String title;

  /** 内容 ，HTML内容 */
  @TableField("content")
  private String content;

  /** 原始内容，即markdown内容 */
  @TableField("originContent")
  private String originContent;

  /** 是否置顶 */
  @TableField("top_flag")
  private Boolean topFlag;

  /** 分类id */
  @TableField("category_id")
  private Integer categoryId;

  private Integer status;
}
