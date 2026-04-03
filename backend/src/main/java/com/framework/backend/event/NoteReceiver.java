package com.framework.backend.event;

import com.framework.backend.model.entity.Note;
import com.framework.backend.service.NoteService;
import com.framework.backend.util.BlogConstant;
import com.framework.backend.service.ElasticSearchService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author fuCong @Date 2023/1/8 19:18
 */
@Component
@RabbitListener(queues = "note")
@Slf4j
public class NoteReceiver {

  @Autowired private ElasticSearchService elasticSearchService;
  @Autowired private NoteService noteService;

  @RabbitHandler
  public void process(Map<String, Object> message) {

    log.info("NoteReceiver消费者收到新增id：" + message.toString());
    // 根据id找出对应的笔记
    Note note = noteService.getNoteById((Integer) message.get("noteId"));
    if (!elasticSearchService.existIndex("note")) {
      elasticSearchService.createIndex("note", BlogConstant.NOTE_MAPPING_SOURCE);
    }
    // elasticSearchService.insert("note", JSON.toJSONString(note));
  }
}
