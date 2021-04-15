package com.tek.core.properties;

import static com.tek.core.constants.TekCoreConstants.TEK_CORE;

import com.tek.core.properties.cors.TekCorsProperties;
import com.tek.core.properties.file.TekFileProperties;
import com.tek.core.properties.i18n.TekLocaleProperties;
import com.tek.core.properties.mail.TekMailProperties;
import com.tek.core.properties.scheduler.TekSchedulerProperties;
import com.tek.core.properties.swagger.TekSwaggerProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Tek Core Module properties to be evaluated from application.yaml / application.properties files.
 *
 * @author MarcoPagan
 */
@ConfigurationProperties(prefix = TEK_CORE)
@Data
public class TekCoreProperties {

  private TekCorsProperties cors = new TekCorsProperties();
  private TekLocaleProperties locale = new TekLocaleProperties();
  private TekFileProperties file = new TekFileProperties();
  private TekMailProperties mail = new TekMailProperties();
  private TekSchedulerProperties scheduler = new TekSchedulerProperties();
  private TekSwaggerProperties swagger = new TekSwaggerProperties();
}


