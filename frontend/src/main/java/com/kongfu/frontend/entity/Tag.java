package com.kongfu.frontend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签实体
 *
 * @author 付聪
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ts_tag")
public class Tag extends Entity {

  @TableField("name")
  private String name;

  /** 该标签下博客的数量 */
  @TableField(exist = false)
  private int count;
}
