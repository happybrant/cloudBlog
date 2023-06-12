package com.kongfu.backend.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/** @author 付聪 */
@Getter
@Setter
@NoArgsConstructor
public class ResponseResult<T> implements Serializable {

  private Integer code = ResponseResultCode.Empty.getValue();

  private String message;

  private T data;

  public ResponseResult(ResponseResultCode code) {
    this.code = code.getValue();
    this.message = "";
    this.data = null;
  }

  public ResponseResult(ResponseResultCode code, String message) {
    this.code = code.getValue();
    this.message = message;
    this.data = null;
  }

  public ResponseResult(ResponseResultCode code, String message, T data) {
    this.code = code.getValue();
    this.message = message;
    this.data = data;
  }

  @Override
  public String toString() {
    return "ResponseResult{"
        + "code="
        + code
        + ", message='"
        + message
        + '\''
        + ", data="
        + data
        + '}';
  }
}
