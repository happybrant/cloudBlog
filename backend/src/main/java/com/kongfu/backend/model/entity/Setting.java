package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** @Author fuCong @Date 2023/2/8 17:18 */
@Data
@TableName(value = "ts_setting")
public class Setting extends Entity {

  @TableField("name")
  private String name;

  @TableField("title")
  private String title;

  @TableField("location")
  private String location;

  @TableField("avatar_url")
  private String avatarUrl;

  /** 个人路由 */
  @TableField("routing")
  private String routing;
  /** 链接模块、归档、分类、最新文章和标签是否展示，1代表展示，0代表不展示 */
  @TableField("link")
  private int link;

  @TableField("archive")
  private int archive;

  @TableField("category")
  private int category;

  @TableField("recentPost")
  private int recentPost;

  @TableField("tag")
  private int tag;

  public Setting(
      int id, String name, String title, String location, String avatarUrl, String routing) {
    super.setId(id);
    this.name = name;
    this.title = title;
    this.location = location;
    this.avatarUrl = avatarUrl;
    this.routing = routing;
  }
}
