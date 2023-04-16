package com.kongfu.backend.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** @author 付聪 */
public class BlogUtil {

  /**
   * 把数字转为中文
   *
   * @param src
   * @return
   */
  public static String int2chineseNum(int src) {
    final String[] num = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    final String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
    String dst = "";
    int count = 0;
    while (src > 0) {
      dst = (num[src % 10] + unit[count]) + dst;
      src = src / 10;
      count++;
    }
    return dst.replaceAll("零[千百十]", "零")
        .replaceAll("零+万", "万")
        .replaceAll("零+亿", "亿")
        .replaceAll("亿万", "亿零")
        .replaceAll("零+", "零")
        .replaceAll("零$", "")
        .replaceAll("一十", "十");
  }

  /**
   * 获取指定日期与当前日期的时间差
   *
   * @param dateTime
   * @return
   * @throws ParseException
   */
  public static String timeSpan(String dateTime) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date fromDate = dateFormat.parse(dateTime);
    Calendar from = Calendar.getInstance();
    from.setTime(fromDate);
    Calendar to = Calendar.getInstance();
    to.setTime(new Date());
    // 只要年月
    int fromYear = from.get(Calendar.YEAR);
    int fromMonth = from.get(Calendar.MONTH);

    int toYear = to.get(Calendar.YEAR);
    int toMonth = to.get(Calendar.MONTH);

    int year = toYear - fromYear;
    int month = toYear * 12 + toMonth - (fromYear * 12 + fromMonth);
    int day = (int) ((to.getTimeInMillis() - from.getTimeInMillis()) / (24 * 3600 * 1000));

    if (year >= 1) {
      return year + " 年前";
    } else {
      if (month >= 6) {
        return month + " 个月前";
      } else {
        return dateTime.substring(0, 10);
      }
    }
  }

  /**
   * 生成随机字符串
   *
   * @return
   */
  public static String generateUUID() {
    // 去除生成的随机字符串中的 ”-“
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * md5 加密
   *
   * @param key 要加密的字符串
   * @return
   */
  public static String md5(String key) {
    if (StringUtils.isBlank(key)) {
      return null;
    }
    return DigestUtils.md5DigestAsHex(key.getBytes());
  }

  /**
   * 生成指定位数的数字随机数, 最高不超过 9 位
   *
   * @param length
   * @return
   */
  public static String getRandomCode(int length) {
    Validate.isTrue(length <= 9 && length > 0, "生成数字随机数长度范围应该在 1~9 内, 参数 length : %s", length);
    int floor = (int) Math.pow(10, length - 1);
    int codeNum = RandomUtils.nextInt(floor, floor * 10);
    return Integer.toString(codeNum);
  }

  /**
   * 获取当前系统时间最近12月的年月（含当月）
   *
   * @return
   */
  public static List<String> getLatest12Month() {
    List<String> monthList = new ArrayList<>();
    Calendar from = Calendar.getInstance();
    from.setTime(new Date());
    for (int i = 0; i < 12; i++) {
      String month = from.get(Calendar.YEAR) + "-" + fillZero(from.get(Calendar.MONTH) + 1);
      monthList.add(0, month);
      from.add(Calendar.MONTH, -1);
    }
    return monthList;
  }

  /**
   * 获取当前系统时间最近一个星期的日期
   *
   * @return
   */
  public static List<Date> getLatestWeek() {
    List<Date> dateList = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    for (int i = 0; i < 7; i++) {
      Date date = calendar.getTime();
      dateList.add(0, date);
      calendar.add(Calendar.DATE, -1);
    }
    return dateList;
  }

  /**
   * 获取当前日期是星期几
   *
   * @param date
   * @return
   */
  public static String getWeekOfDate(Date date) {
    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0) {
      w = 0;
    }
    return weekDays[w];
  }

  /** 格式化月份 */
  public static String fillZero(int i) {
    String month = "";
    if (i < 10) {
      month = "0" + i;
    } else {
      month = String.valueOf(i);
    }
    return month;
  }
}
