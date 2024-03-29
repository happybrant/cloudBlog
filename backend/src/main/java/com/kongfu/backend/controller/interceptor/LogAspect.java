package com.kongfu.backend.controller.interceptor;

import com.kongfu.backend.annotation.Log;
import com.kongfu.backend.model.entity.AccessLog;
import com.kongfu.backend.model.vo.HostHolder;
import com.kongfu.backend.model.vo.LoginToken;
import com.kongfu.backend.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/** @Author fuCong @Date 2023/4/16 20:30 */
@Component
@Aspect
@Slf4j
public class LogAspect {
  ThreadLocal<AccessLog> logThreadLocal = new ThreadLocal<>();
  @Autowired AccessLogService accessLogService;
  @Autowired HostHolder holder;

  @Before("@annotation(com.kongfu.backend.annotation.Log)")
  public void before(JoinPoint joinPoint) throws Exception {
    // 得到 HttpServletRequest
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    assert attributes != null;
    HttpServletRequest request = attributes.getRequest();
    log.debug("============ before ==========");
    // 获取Log注解信息
    AccessLog accessLog = getWebLogInfo(joinPoint);
    // 设置请求地址URL
    accessLog.setUrl(request.getRequestURL().toString());
    // 设置请求Ip
    accessLog.setIp(request.getRemoteAddr());
    // 设置请求参数
    if ("新增笔记".equals(accessLog.getDescription()) || "修改笔记".equals(accessLog.getDescription())) {
      accessLog.setParams("笔记内容");
    } else if ("新增博客".equals(accessLog.getDescription())
        || "修改博客".equals(accessLog.getDescription())) {
      accessLog.setParams("博客内容");
    } else {
      accessLog.setParams(Arrays.toString(joinPoint.getArgs()));
    }
    logThreadLocal.set(accessLog);
  }

  @Around("@annotation(com.kongfu.backend.annotation.Log)")
  public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    log.debug("============ doAround ==========");
    long startTime = System.currentTimeMillis();
    Object result = proceedingJoinPoint.proceed();
    AccessLog accessLog = logThreadLocal.get();
    accessLog.setRequestTime(new Date(startTime));
    LoginToken loginToken = holder.getUser();
    if (loginToken != null) {
      accessLog.setUsername(loginToken.getLoginName());
    }
    // 设置执行时间
    accessLog.setTotalMillis(System.currentTimeMillis() - startTime);
    if (accessLogService.addAccessLog(accessLog) > 0) {
      logThreadLocal.remove();
    }
    return result;
  }

  /**
   * 获取web日志注解信息
   *
   * @param joinPoint
   * @return
   * @throws Exception
   */
  public AccessLog getWebLogInfo(JoinPoint joinPoint) throws Exception {
    AccessLog accessLog = new AccessLog();
    // 获取切入点的目标类
    String targetName = joinPoint.getTarget().getClass().getName();
    Class<?> targetClass = Class.forName(targetName);
    // 获取切入方法名
    String methodName = joinPoint.getSignature().getName();
    // 获取切入方法参数
    Object[] arguments = joinPoint.getArgs();
    // 获取目标类的所有方法
    Method[] methods = targetClass.getMethods();
    for (Method method : methods) {
      // 方法名相同、包含目标注解、方法参数个数相同（避免有重载）
      if (method.getName().equals(methodName)
          && method.isAnnotationPresent(Log.class)
          && method.getParameterTypes().length == arguments.length) {
        String menu = method.getAnnotation(Log.class).menu();
        String description = method.getAnnotation(Log.class).description();
        accessLog.setMenu(menu);
        accessLog.setDescription(description);
        return accessLog;
      }
    }
    return accessLog;
  }
}
