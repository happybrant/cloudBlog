package com.kongfu.backend.event;

import com.alibaba.fastjson.JSONObject;
import com.kongfu.backend.model.vo.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 事件的生产者
 *
 * @author 付聪
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate template;

    public void fireEvent(Event event) {
        template.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
