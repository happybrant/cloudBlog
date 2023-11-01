package com.kongfu.backend.common.exception;

import com.kongfu.backend.common.ResponseResultCode;

/** @author fucong @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "业务处理异常") */
public class BusinessException extends MyBaseException {
  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(ResponseResultCode resultCode) {
    super(resultCode.getMessage());
  }
}
