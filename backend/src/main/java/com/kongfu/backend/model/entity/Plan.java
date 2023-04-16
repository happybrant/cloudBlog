package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author fuCong
 * @version 1.0.0 @Description 计划实体
 * @createTime 2022-06-21 11:04:00
 */
@Data
@NoArgsConstructor
@TableName(value = "ts_plan")
public class Plan extends Entity {

  /** 父计划id */
  private int parentId;
  /** 名称 */
  @TableField("name")
  private String name;

  /** 描述 */
  @TableField("description")
  private int description;

  /** 顺序 */
  @TableField("order")
  private int order;

  /** 是否上线 */
  private int onlineFlag;

  /** 计划完成时间 */
  @TableField("end_time")
  private Date finishTime;
}
