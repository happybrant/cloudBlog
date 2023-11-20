package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 基础实体类
 *
 * @author 付聪
 */
@Data
public class Entity {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  /** 创建人 */
  @TableField(value = "create_user", fill = FieldFill.INSERT)
  private Integer createUser;
  /** 最后修改人 */
  @TableField(value = "last_update_user", fill = FieldFill.INSERT_UPDATE)
  private Integer lastUpdateUser;
  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;
  /** 最后修改时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
  private Date lastUpdateTime;
  /** 状态 */
  @TableField(value = "status", fill = FieldFill.INSERT)
  private Integer status;
}
