package com.kongfu.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @Author fuCong @Date 2023/4/16 20:29 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
  /**
   * 菜单
   *
   * @return
   */
  String menu() default "";
  /**
   * 操作描述
   *
   * @return
   */
  String description() default "";
}
