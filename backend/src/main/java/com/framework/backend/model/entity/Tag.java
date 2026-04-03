package com.framework.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import lombok.Data;

/**
 * 标签实体
 *
 * @author 付聪
 */
@Data
@TableName(value = "ts_tag")
public class Tag extends MyBaseEntity {

  @TableField("name")
  private String name;

  private Integer status;

  /** 该标签下博客的数量 */
  @TableField(exist = false)
  private int count;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
