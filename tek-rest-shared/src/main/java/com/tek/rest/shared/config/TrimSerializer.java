package com.tek.rest.shared.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.SneakyThrows;

public class TrimSerializer extends StdDeserializer<String> {

  protected TrimSerializer(Class<?> vc) {
    super(vc);
  }

  @Override
  @SneakyThrows
  public String deserialize(JsonParser jsonParser, DeserializationContext ctx) {
    return jsonParser.getText() != null ? jsonParser.getText().trim() : null;
  }

}
