package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fuCong
 * @version 1.0.0 @Description 任务实体
 * @createTime 2022-06-21 11:04:00
 */
@Data
@NoArgsConstructor
@TableName(value = "ts_task")
public class Task extends Entity {
    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 任务类型
     */
    @TableField("type")
    private int type;

    /**
     * 优先级
     */
    @TableField("level")
    private int level;

    /**
     * 任务描述
     */
    @TableField("description")
    private String description;

    /**
     * 任务开始时间
     */
    @TableField("start_time")
    private String startTime;

    /**
     * 任务结束时间
     */
    @TableField("end_time")
    private String endTime;
}
