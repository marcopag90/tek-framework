package com.tek.rest.shared.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class TrimSerializer extends StdDeserializer<String> {

  protected TrimSerializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
    return jsonParser.getText() != null ? jsonParser.getText().trim() : null;
  }

}
