package com.kongfu.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户
 *
 * @author 付聪
 */
@Data
@TableName(value = "ts_user")
public class User extends Entity {

    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    /**
     * 显示名
     */
    @TableField("displayName")
    private String displayName;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    /**
     * 盐值，六位随机数字，用于加密密码
     */
    @TableField("salt")
    private String salt;
    /**
     * 邮箱地址
     */
    @TableField("email")
    private String email;
    /**
     * 用户类型，1-管理员;2-普通用户
     */
    @TableField("type")
    private Integer type;
    /**
     * 头像地址
     */
    @TableField("header_url")
    private String headerUrl;
}
