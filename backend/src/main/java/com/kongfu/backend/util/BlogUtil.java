package com.kongfu.backend.util;

import com.alibaba.fastjson.JSONObject;
import com.kongfu.backend.model.entity.User;
import com.kongfu.backend.model.vo.LoginTicket;
import com.kongfu.backend.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 付聪
 */
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
     * 将服务端返回的消息封装成 JSON 格式的字符串
     *
     * @param code 状态码
     * @param msg  提示消息
     * @param map  业务数据
     * @return 返回 JSON 格式字符串
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    // 重载 getJSONString 方法，服务端方法可能不返回业务数据
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    // 重载 getJSONString 方法，服务端方法可能不返回业务数据和提示消息
    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    // editor.md 要求返回的 JSON 字符串格式
    public static String getEditorMdJSONString(int success, String message, String url) {
        JSONObject json = new JSONObject();
        json.put("success", success);
        json.put("message", message);
        json.put("url", url);
        return json.toJSONString();
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
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Jack");
        map.put("age", 18);
        // {"msg":"ok","code":0,"name":"Jack","age":18}
        System.out.println(getJSONString(0, "ok", map));
    }

    /**
     * 设置权限
     *
     * @param ticket
     * @param userService
     */
    public static void setContext(String ticket, UserService userService) {
        if (ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证状态（是否）是否过期
            if (loginTicket != null
                    && loginTicket.getStatus() == 1
                    && loginTicket.getExpired().after(new Date())) {
                User user = userService.findUserById(loginTicket.getUserId());
                // 构建用户认证的结果，并存入 SecurityContext, 以便于 Spring Security 进行授权
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
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

    /**
     * 格式化月份
     */
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
