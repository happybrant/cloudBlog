package com.kongfu.frontend.entity;

import java.util.List;

/**
 * 分类实体
 */
public class Category extends  Entity{

    private String name;
    private int parentId;
    private int order;
    //分类下博客的数量
    private int count;
    //分类下的子分类
    List<Category> categoryList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
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

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
