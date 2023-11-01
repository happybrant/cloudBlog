package com.kongfu.backend.common;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author fuCong
 * @version 1.0.0 @Description TODO
 * @createTime 2022-05-25 17:15:00
 */
public enum ResponseResultCode {
  /** 空 */
  Empty(0, "空值"),
  /** 成功 */
  Success(200, "成功"),
  /** 错误 */
  Error(500, "系统内部错误"),
  /** 参数为空 */
  ParameterEmpty(10000, "参数为空"),
  /** 权限不够 */
  AuthFailed(401, "权限不足"),
  /** 服务异常 */
  Busy(503, "服务不可用");

  @ApiModelProperty("code")
  private int code;

  @ApiModelProperty("message")
  private String message;

  ResponseResultCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
