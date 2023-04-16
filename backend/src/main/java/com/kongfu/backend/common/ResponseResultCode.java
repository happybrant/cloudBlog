package com.kongfu.backend.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author fuCong
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022-05-25 17:15:00
 */
public enum ResponseResultCode {
    /**
     * 空
     */
    Empty(0),
    /**
     * 成功
     */
    Success(200),
    /**
     * 错误
     */
    Error(-1),
    /**
     * 参数为空
     */
    ParameterEmpty(10000),
    /**
     * 权限不够
     */
    AuthFailed(401),
    /**
     * 服务异常
     */
    Busy(503);

    @ApiModelProperty("value")
    private int value;

    ResponseResultCode(int value) {
        this.value = value;
    }


    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}