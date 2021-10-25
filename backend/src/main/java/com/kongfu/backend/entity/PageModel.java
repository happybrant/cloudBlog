package com.kongfu.backend.entity;


/**
 * 封装分页相关的信息
 */
public class PageModel {
    private int total;
    private Object rows;

    public int getTotal() {
        return total;
    }

    public Object getRows() {
        return rows;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
