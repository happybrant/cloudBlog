package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author fuCong
 * @version 1.0.0 @Description 相册实体
 * @createTime 2022-06-13 11:04:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName(value = "ts_album")
public class Album extends Entity {
  /** 相册名称 */
  @TableField("name")
  private String name;

  /** 相册描述 */
  @TableField("description")
  private String description;

  /** 相册图片 */
  @TableField("image_url")
  private String imageUrl;
}
