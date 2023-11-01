package com.kongfu.backend.common.exception;

import com.kongfu.backend.common.ResponseResult;
import com.kongfu.backend.common.ResponseResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** @author fucong extends ResponseEntityExceptionHandler */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 系统中的自定义业务异常
   *
   * @param request
   * @param response
   * @param businessException
   * @return
   */
  @ExceptionHandler(BusinessException.class)
  @ResponseBody
  public Object businessException(
      HttpServletRequest request,
      HttpServletResponse response,
      BusinessException businessException) {
    // 设置HTTP状态码
    response.setStatus(ResponseResultCode.Success.getCode());
    // 设置前端返回值
    ResponseResult result = new ResponseResult();
    // 当ResponseResultCode存在时按它返回异常信息
    ResponseResultCode resultCode = businessException.getResultCode();
    if (resultCode != null) {
      result.setCode(resultCode.getCode());
      result.setMessage(resultCode.getMessage());
    }
    // 当ResponseResultCode不存在时就按message异常信息
    else {
      result.setCode(ResponseResultCode.Error.getCode());
      result.setMessage(businessException.getMessage());
    }
    return result;
  }
}
