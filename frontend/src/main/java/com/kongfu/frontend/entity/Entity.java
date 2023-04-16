package com.kongfu.frontend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/** @author 付聪 */
@Data
public class Entity {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  /** 创建人 */
  @TableField("create_user")
  private Integer createUser;
  /** 最后修改人 */
  @TableField("last_update_user")
  private Integer lastUpdateUser;
  /** 创建时间 */
  @TableField("create_time")
  private String createTime;
  /** 最后修改时间 */
  @TableField("last_update_time")
  private String lastUpdateTime;
  /** 状态 */
  @TableField("status")
  private Integer status;
}
