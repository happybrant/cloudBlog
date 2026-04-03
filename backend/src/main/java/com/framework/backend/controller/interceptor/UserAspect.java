package com.framework.backend.controller.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.framework.backend.model.dto.BaseQuery;
import com.framework.backend.model.entity.User;
import com.framework.backend.util.BlogConstant;
import com.framework.backend.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author fuCong @Date 2023/5/19 15:08 为所有select查询加上当前用户id作为条件
 */
@Component
@Aspect
public class UserAspect {

  // @Autowired private HostHolder holder;

  /** mybatis-plus中的mapper切入点 */
  //  @Pointcut("execution(public * com.baomidou.mybatisplus.core.mapper.BaseMapper.select*(..)) ")
  //  public void baseMapperAspect() {}

  /** 自定义mapper切入点 */
  @Pointcut("execution(public * com.framework.backend.dao.*.select*(..))")
  public void customMapperAspect() {}

  @Before("customMapperAspect()")
  @Order(1)
  public void beforeCustomMapper(JoinPoint joinPoint) {
    User user = SecurityUtils.getCurrentUser();
    // 表示当前角色不是管理员，加上当前登录用户作为条件
    if (user != null && user.getRoleCodes().contains(BlogConstant.ROLE_ADMIN)) {
      // 获取参数
      Object[] objects = joinPoint.getArgs();
      if (objects.length > 0 && objects[0] instanceof BaseQuery baseQuery) {
        baseQuery.setCreateUser(user.getId());
      }
    }
  }

  @Before("baseMapperAspect()")
  @Order(2)
  public void beforeBaseMapper(JoinPoint joinPoint) {
    User user = SecurityUtils.getCurrentUser();

    // 加上当前登录用户作为条件
    if (user != null) {
      // 获取参数
      Object[] objects = joinPoint.getArgs();
      QueryWrapper<?> queryWrapper;
      // 获得访问的方法名
      String methodName = joinPoint.getSignature().getName();
      if (methodName.endsWith("Page")) {
        // 如果是带分页的查询，则第二个参数是查询条件
        queryWrapper = (QueryWrapper<?>) objects[1];
        queryWrapper.eq("create_user", user.getId());
      } else {
        // baseMapper中还有selectById之类的查询方法，此类方法的参数为int，故需要先判断一下
        if (objects[0] instanceof QueryWrapper<?>) {
          queryWrapper = (QueryWrapper<?>) objects[0];
          queryWrapper.eq("create_user", user.getId());
        }
      }
    }
  }
}
