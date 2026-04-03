package com.framework.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 付聪
 */
public class CookieUtil {

  /**
   * 从 request 中获取指定 name 的 cookie
   *
   * @param request
   * @param name
   * @return
   */
  public static String getValue(HttpServletRequest request, String name) {
    if (request == null || StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("参数为空");
    }
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (name.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
