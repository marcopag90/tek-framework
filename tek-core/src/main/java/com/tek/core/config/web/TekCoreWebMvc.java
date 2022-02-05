package com.tek.core.config.web;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_WEB_MVC_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_WEB_MVC_FILE_TIMESTAMP;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_WEB_MVC_TIMESTAMP;

import com.tek.core.TekCoreAutoConfig;
import java.text.SimpleDateFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(TEK_CORE_WEB_MVC_CONFIGURATION)
@ConditionalOnClass(TekCoreAutoConfig.class)
public class TekCoreWebMvc {

  public static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String FILE_TIMESTAMP = "yyyyMMdd_HHmmss_SSS";

  @Bean(TEK_CORE_WEB_MVC_TIMESTAMP)
  public SimpleDateFormat timestampSimpleDateFormat() {
    return new SimpleDateFormat(TIMESTAMP);
  }

  @Bean(TEK_CORE_WEB_MVC_FILE_TIMESTAMP)
  public SimpleDateFormat fileTimestampFormat() {
    return new SimpleDateFormat(FILE_TIMESTAMP);
  }
}
