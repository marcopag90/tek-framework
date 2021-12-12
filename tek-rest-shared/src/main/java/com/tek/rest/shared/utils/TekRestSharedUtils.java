package com.tek.rest.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

public class TekRestSharedUtils {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.registerModules(new JavaTimeModule());
  }

  private TekRestSharedUtils() {
  }

  /**
   * Converts a Java Object to a serializable String.
   */
  @SneakyThrows
  public static String asJsonString(final Object obj) {
    return objectMapper.writeValueAsString(obj);
  }

}
