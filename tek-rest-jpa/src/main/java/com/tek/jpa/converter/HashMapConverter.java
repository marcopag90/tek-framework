package com.tek.jpa.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import javax.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hashmap String Type Conversion.
 * <p>
 * This implementation converts a String representation of a map in a database (like plain JSON
 * objects) to a more friendly user HashMap java type.
 *
 * @author MarcoPagan
 */
public class HashMapConverter implements AttributeConverter<HashMap<String, Object>, String> {

  private final Logger log = LoggerFactory.getLogger(HashMapConverter.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(HashMap<String, Object> attribute) {
    String text = null;
    try {
      text = objectMapper.writeValueAsString(attribute);
    } catch (Exception ex) {
      log.error("Writing error: {}", attribute);
    }
    return text;
  }

  @SuppressWarnings("unchecked")
  @Override
  public HashMap<String, Object> convertToEntityAttribute(String dbData) {
    HashMap<String, Object> map = new HashMap<>();
    try {
      map = objectMapper.readValue(dbData, map.getClass());
    } catch (Exception ex) {
      log.error("Reading error: {}", dbData);
    }
    return map;
  }
}
