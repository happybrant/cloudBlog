package com.kongfu.backend.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装分页相关的信息
 *
 * @author 付聪
 */
@Data
public class PageModel<T> {
    private int total;

    private List<T> rows;

    private Integer pageIndex = 1;

    private Integer pageSize = 10;
}
