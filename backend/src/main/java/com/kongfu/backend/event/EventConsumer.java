package com.kongfu.backend.event;

import com.alibaba.fastjson.JSONObject;
import com.kongfu.backend.model.vo.Event;
import com.kongfu.backend.model.vo.Message;
import com.kongfu.backend.service.MessageService;
import com.kongfu.backend.util.BlogConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.HashMap;
import java.util.Map;

/** @author 付聪 事件消费者 */
public class EventConsumer implements BlogConstant {

  @Autowired private MessageService messageService;

  /**
   * 消费发布博客事件
   *
   * @param record
   */
  @KafkaListener(topics = {TOPIC_PUBLISH})
  private void handleMessage(ConsumerRecord record) {
    if (record == null || record.value() == null) {
      return;
    }
    Event event = JSONObject.parseObject(record.value().toString(), Event.class);
    if (event == null) {
      return;
    }
    // 发送系统通知
    Message message = new Message();
    // message.setFromId(1);
    // message.setToId(event.getEntityUserId());
    message.setConversationId(event.getTopic());

    Map<String, Object> content = new HashMap<>(16);
    content.put("userId", event.getUserId());
    content.put("entityType", event.getEntityType());
    content.put("entityId", event.getEntityId());
    if (!event.getData().isEmpty()) {
      event.getData().forEach(content::put);
    }
    message.setContent(JSONObject.toJSONString(content));

    messageService.addMessage(message);
  }
}
