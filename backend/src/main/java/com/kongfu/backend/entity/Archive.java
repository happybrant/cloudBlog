package com.kongfu.backend.entity;

/**
 * 按年份/月份对博客进行分类
 * @author 付聪
 */
public class Archive {
    private String year;
    private String month;
    private int count;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Archive(String year, String month, int count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }
}
