package com.kongfu.backend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：xutengfei
 * @date ：Created in 2021/11/11 19:04
 * @description：
 */
@Data
@NoArgsConstructor
public class UserQuery {
    /**
     * 支持用户名和展示名查询
     */
    private String name;

    private Integer type;

    private Integer status;

    private Integer pageSize;

    private Integer pageIndex;
}
