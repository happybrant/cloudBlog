package com.kongfu.backend.Config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.model.vo.LoginToken;
import com.kongfu.backend.util.BlogConstant;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/** @author 付聪 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
  private static final Logger logger = LoggerFactory.getLogger(MyMetaObjectHandler.class);
  @Resource public HostHolder holder;

  @Override
  /** 创建记录时还需要同时修改更新相关的字段信息 */
  public void insertFill(MetaObject metaObject) {
    logger.debug("start insert fill ....");
    // 注意这里的fieldName是实体字段名称，而不是数据库字段名称！
    this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
    this.strictInsertFill(metaObject, "lastUpdateTime", Date.class, new Date());
    this.strictInsertFill(metaObject, "status", Integer.class, BlogConstant.PUBLISH_STATUS);
    // 存在用户登录信息才自动填值
    LoginToken user = holder.getUser();
    if (user != null) {
      int userId = user.getId();
      this.strictInsertFill(metaObject, "createUser", Integer.class, userId);
      this.strictInsertFill(metaObject, "lastUpdateUser", Integer.class, userId);
    }
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    logger.debug("start update fill ....");
    this.strictUpdateFill(metaObject, "lastUpdateTime", Date.class, new Date());
    // 存在用户登录信息才自动填值，方便定时任务之类不需要登录的功能使用
    LoginToken user = holder.getUser();
    if (user != null) {
      int userId = user.getId();
      this.strictUpdateFill(metaObject, "lastUpdateUser", Integer.class, userId);
    }
  }

  /**
   * 默认填充策略为如果属性有值则不覆盖,现在改为每次都覆盖更新
   *
   * @param metaObject
   * @param fieldName
   * @param fieldVal
   * @return
   */
  @Override
  public MetaObjectHandler fillStrategy(MetaObject metaObject, String fieldName, Object fieldVal) {
    setFieldValByName(fieldName, fieldVal, metaObject);
    return this;
  }
}
