package com.kongfu.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于存放在es的博客模型
 *
 * @author 付聪
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
  private int id;
  /** 标题 */
  private String title;
  /** 内容 ，纯文本，剔除了HTML标签 */
  private String content;
  /** 博客作者id */
  private int createUser;
}
