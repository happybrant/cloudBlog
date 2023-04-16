package com.kongfu.backend.common;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.lang.reflect.Type;

/** @author 付聪 */
public class BaseEnumCodec implements ObjectSerializer, ObjectDeserializer {
  @Override
  public void write(
      JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
    if (object == null) {
      serializer.write("");
    } else {
      serializer.write(((ResponseResultCode) object).getValue());
    }
  }

  @Override
  public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    return null;
  }

  @Override
  public int getFastMatchToken() {
    return 0;
  }
}
