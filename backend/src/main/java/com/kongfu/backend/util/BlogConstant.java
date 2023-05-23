package com.kongfu.backend.util;

/**
 * 常量
 *
 * @author 付聪
 */
public interface BlogConstant {

  int RETURN_SUCCESS = 1;
  int RETURN_FAILURE = 0;
  String RETURN_SUCCESS_DESC = "操作成功";
  /** 登陆凭证 */
  String TICKET = "ticket";
  /** 博客删除状态 */
  int DELETE_STATUS = 0;
  /** 博客发布状态 */
  int PUBLISH_STATUS = 1;
  /** 博客未发布状态;暂存状态 */
  int UN_PUBLISH_STATUS = 2;
  /** 关于我类型博客 */
  int ABOUT_ME_STATUS = 3;
  /** 关于我类型博客 */
  int ABOUT_ME_UN_PUBLISH_STATUS = 4;
  /** 任务删除状态 */
  int TASK_DELETE_STATUS = 0;
  /** 任务待办状态 */
  int TASK_TODO_STATUS = 1;
  /** 任务进行中状态 */
  int TASK_WORKING_STATUS = 2;
  /** 任务完成中状态 */
  int TASK_DONE_STATUS = 3;

  /** 默认的登录凭证超时时间 (12小时) */
  int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

  /** 记住我状态下的凭证超时时间 (100天) */
  int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

  /** 权限：普通用户 */
  int AUTHORITY_USER = 2;

  /** 权限：管理员 */
  int AUTHORITY_ADMIN = 1;

  /** Kafka 主题：发布新博客 */
  String TOPIC_PUBLISH = "publish";

  /** 实体类型：博客 */
  int ENTITY_TYPE_BLOG = 1;

  /** token黑名单前缀 */
  String TOKEN_BLACKLIST_PREFIX = "logout_";

  String INITIAL_PASSWORD = "123456";
  String ARTICLE_INDEX = "article";
  String NOTE_MAPPING_SOURCE =
      "{\n"
          + "    \"properties\":{\n"
          + "        \"title\":{\n"
          + "            \"type\":\"text\",\n"
          + "            \"analyzer\":\"ik_max_word\"\n"
          + "        },\n"
          + "        \"content\":{\n"
          + "            \"type\":\"text\"\n"
          + "        },\n"
          + "        \"originContent\":{\n"
          + "            \"type\":\"text\"\n"
          + "        },\n"
          + "        \"id\":{\n"
          + "            \"type\":\"integer\"\n"
          + "        },\n"
          + "        \"createUser\":{\n"
          + "            \"type\":\"integer\"\n"
          + "        },\n"
          + "        \"lastUpdateUser\":{\n"
          + "            \"type\":\"integer\"\n"
          + "        },\n"
          + "        \"createTime\":{\n"
          + "            \"type\":\"date\",\n"
          + "            \"format\":\"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd\"\n"
          + "        },\n"
          + "        \"lastUpdateTime\":{\n"
          + "            \"type\":\"date\",\n"
          + "            \"format\":\"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd\"\n"
          + "        },\n"
          + "        \"status\":{\n"
          + "            \"type\":\"integer\"\n"
          + "        }\n"
          + "    }\n"
          + "}";
  /** 博客再es中的索引 */
  String ARTICLE_MAPPING_SOURCE =
      "{\n"
          + "    \"properties\":{\n"
          + "        \"title\":{\n"
          + "            \"type\":\"text\",\n"
          + "            \"analyzer\":\"ik_max_word\"\n"
          + "        },\n"
          + "        \"content\":{\n"
          + "            \"type\":\"text\"\n"
          + "        },\n"
          + "        \"status\":{\n"
          + "            \"type\":\"integer\"\n"
          + "        }\n"
          + "    }\n"
          + "}";
}
