package com.tek.rest.shared;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility methods to be used in test cases.
 *
 * @author MarcoPagan
 */
public class TekRestSharedUtils {

  private TekRestSharedUtils() {
  }

  /**
   * Converts a Java Object to a serializable String.
   */
  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
