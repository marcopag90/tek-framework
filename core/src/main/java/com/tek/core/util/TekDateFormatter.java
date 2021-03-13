package com.tek.core.util;

import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_FILE_TIMESTAMP_BEAN;
import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_TIMESTAMP_BEAN;

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

  @Bean(TEK_CORE_TIMESTAMP_BEAN)
  public SimpleDateFormat timestampSimpleDateFormat() {
    return new SimpleDateFormat(TIMESTAMP);
  }

  @Bean(TEK_CORE_FILE_TIMESTAMP_BEAN)
  public SimpleDateFormat fileTimestampFormat() {
    return new SimpleDateFormat(FILE_TIMESTAMP);
  }
}
