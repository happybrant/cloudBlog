package com.framework.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客类实体
 *
 * @author 付聪
 */
@Data
@NoArgsConstructor
@TableName(value = "ts_article")
public class Article extends MyBaseEntity {

  /** 标题 */
  @TableField("title")
  private String title;

  /** 内容 ，HTML内容 */
  @TableField("content")
  private String content;

  /** 原始内容，即markdown内容 */
  @TableField("origin_content")
  private String originContent;

  @TableField("description")
  private String description;

  /** 封面图片地址 */
  @TableField("cover_url")
  private String coverUrl;

  @TableField("category_id")
  private String categoryId;

  private Integer status;

  /** 博客作者名称 */
  @TableField(exist = false)
  private String createUsername;

  /** 分类名称 */
  @TableField(exist = false)
  private String categoryName;

  @TableField(exist = false)
  private String[] tagIds;

  @TableField(exist = false)
  private List<Tag> tags;
}
