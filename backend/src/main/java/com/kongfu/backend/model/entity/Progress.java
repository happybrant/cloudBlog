package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fuCong
 * @version 1.0.0 @Description 任务进度实体
 * @createTime 2022-06-29 18:25:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "ts_progress")
public class Progress extends Entity {
    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 任务ID
     */
    @TableField("task_id")
    private int taskId;
}
