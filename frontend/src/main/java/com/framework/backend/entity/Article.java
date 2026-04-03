package com.framework.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.framework.backend.common.MyBaseEntity;
import java.util.List;
import lombok.Data;

/**
 * 博客类实体
 *
 * @author 付聪
 */
@Data
public class Article extends MyBaseEntity {

  private String title;
  private String content;
  private String description;
  private int categoryId;
  private String coverUrl;

  @TableField(exist = false)
  private int[] tagIds;

  @TableField(exist = false)
  private List<String> tagNames;

  private int status;

  /** 分类路径 */
  private String path;

  /** 前一篇 */
  private Article preArticle;

  /** 后一篇 */
  private Article nextArticle;
}
