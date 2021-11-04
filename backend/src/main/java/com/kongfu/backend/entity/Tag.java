package com.kongfu.backend.entity;


/**
 * 标签实体
 */
public class Tag extends  Entity{

    private String name;
    private String code;
    private int order;
    /**
     *  该标签下博客的数量
     */
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
