package com.tek.core.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HashMapConverterTest {

  private final HashMapConverter converter = new HashMapConverter();
  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  @SneakyThrows
  void convertToDatabaseColumn() {
    val map = new HashMap<String, Object>();
    map.put("integerValue", 1);
    map.put("stringValue", "1");
    val value = converter.convertToDatabaseColumn(map);
    val node = mapper.readTree(value);
    Assertions.assertAll(
        () -> assertEquals(1, node.get("integerValue").asInt()),
        () -> assertEquals("1", node.get("stringValue").asText())
    );
  }

  @Test
  @SneakyThrows
  void convertToEntityAttribute() {
    val node = mapper.createObjectNode();
    node.put("integerValue", 1);
    node.put("stringValue", "1");
    val toConvert = mapper.writeValueAsString(node);
    val value = converter.convertToEntityAttribute(toConvert);
    Assertions.assertAll(
        () -> assertEquals(node.get("integerValue").asInt(), value.get("integerValue")),
        () -> assertEquals(node.get("stringValue").asText(), value.get("stringValue"))
    );
  }
}
