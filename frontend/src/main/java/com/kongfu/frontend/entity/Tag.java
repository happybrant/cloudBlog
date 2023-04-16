package com.kongfu.frontend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** 标签实体 */
@Data
@TableName(value = "ts_tag")
public class Tag extends Entity {

  @TableField("name")
  private String name;

  /** 该标签下博客的数量 */
  @TableField(exist = false)
  private int count;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
