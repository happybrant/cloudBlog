package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fuCong
 * @version 1.0.0 @Description 图片
 * @createTime 2022-06-13 11:13:00
 */
@Data
@NoArgsConstructor
@TableName(value = "ts_photo")
public class Photo extends Entity {
    /**
     * 图片路径
     */
    @TableField("path")
    private String path;
    /**
     * 所属相册id
     */
    @TableField("album_id")
    private int albumId;
    /**
     * 图片描述
     */
    @TableField("description")
    private String description;

    public Photo(String path, int albumId, String description) {
        this.path = path;
        this.albumId = albumId;
        this.description = description;
    }
}
