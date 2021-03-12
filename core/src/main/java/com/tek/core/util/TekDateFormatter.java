package com.tek.core.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * Configuration to provide different date formatters.
 */
@Configuration
public class TekDateFormatter {

  public static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String FILE_TIMESTAMP = "yyyyMMdd_HHmmss_SSS";

  @Bean
  public SimpleDateFormat withTimestamp() {
    return new SimpleDateFormat(TIMESTAMP);
  }

  @Bean
  public SimpleDateFormat fileDateFormat() {
    return new SimpleDateFormat(FILE_TIMESTAMP);
  }
}