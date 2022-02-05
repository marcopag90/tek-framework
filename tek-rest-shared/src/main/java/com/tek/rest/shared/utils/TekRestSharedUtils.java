package com.tek.rest.shared.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class TekRestSharedUtils {

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
  public static String asJsonString(final Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }

}
