package com.framework.backend.mapper;

import com.framework.backend.model.vo.Message;

/**
 * @author 付聪
 */
public interface MessageMapper {
  /**
   * 新增一条私信
   *
   * @param message
   * @return
   */
  int insertMessage(Message message);
}
