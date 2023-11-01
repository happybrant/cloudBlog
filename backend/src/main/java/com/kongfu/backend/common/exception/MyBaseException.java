package com.kongfu.backend.common.exception;

import com.kongfu.backend.common.ResponseResultCode;

/** @author fucong */
public class MyBaseException extends RuntimeException {
  protected ResponseResultCode resultCode;

  public MyBaseException() {
    super();
  }

  public MyBaseException(String message) {
    super(message);
  }

  public MyBaseException(ResponseResultCode resultCode) {
    super(resultCode.getMessage());
    this.resultCode = resultCode;
  }

  public ResponseResultCode getResultCode() {
    return resultCode;
  }

  public void setResultCode(ResponseResultCode resultCode) {
    this.resultCode = resultCode;
  }
}
