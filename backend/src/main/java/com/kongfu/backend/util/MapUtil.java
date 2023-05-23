package com.kongfu.backend.util;

import com.alibaba.druid.util.StringUtils;

import java.util.Map;

/** @Author fuCong @Date 2023/5/11 16:46 处理map中的数据 */
public class MapUtil {

  public static String getValueAsString(Map<String, Object> map, String key) {
    return getValueAsString(map, key, "");
  }

  public static String getValueAsString(Map<String, Object> map, String key, String defaultValue) {
    String value = defaultValue;

    if (map != null && !StringUtils.isEmpty(key) && map.containsKey(key) && map.get(key) != null) {
      value = map.get(key).toString();
    }
    return value;
  }

  public static Integer getValueAsInteger(Map<String, Object> map, String key) {
    return getValueAsInteger(map, key, 0);
  }

  public static Integer getValueAsInteger(
      Map<String, Object> map, String key, Integer defaultValue) {
    Integer value = defaultValue;

    String str = getValueAsString(map, key);
    if (StringUtils.isNumber(str)) {
      value = Integer.valueOf(str);
    }

    return value;
  }
}
