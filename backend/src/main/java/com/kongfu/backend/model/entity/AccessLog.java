package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 系统操作日志 @Author fuCong @Date 2023/4/27 16:40 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "ts_log")
public class AccessLog extends Entity {
  /** 访问url */
  @TableField("url")
  private String url;
  /** 访问菜单 */
  @TableField("menu")
  private String menu;
  /** 接口描述 */
  @TableField("description")
  private String description;
  /** 操作耗时 */
  @TableField("totalMillis")
  private long totalMillis;
  /** 访问ip */
  @TableField("ip")
  private String ip;
  /** 请求参数 */
  @TableField("params")
  private String params;
}
