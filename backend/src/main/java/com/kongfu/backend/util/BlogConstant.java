package com.kongfu.backend.util;

/**
 * 常量
 * @author 付聪
 */
public interface BlogConstant {

    int RETURN_SUCCESS = 1;
    int RETURN_FAILURE = 0;
    String RETURN_SUCCESS_DESC = "操作成功";
    /**
     * 登陆凭证
     */
    String TICKET  = "ticket";
    /**
     * 博客删除状态
     */
    int DELETE_STATUS = 0;
    /**
     * 博客发布状态
     */
    int PUBLISH_STATUS = 1;
    /**
     * 博客未发布状态;暂存状态
     */
    int UN_PUBLISH_STATUS = 2;

    /**
     * 默认的登录凭证超时时间 (12小时)
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住我状态下的凭证超时时间 (100天)
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 权限：普通用户
      */
    String AUTHORITY_USER = "user";

    /**
     * 权限：管理员
     */
    String AUTHORITY_ADMIN = "admin";

}
