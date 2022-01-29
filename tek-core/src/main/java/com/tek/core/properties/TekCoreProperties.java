package com.tek.core.properties;

import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PREFIX;

import com.tek.core.properties.file.TekFileProperties;
import com.tek.core.properties.i18n.TekLocaleProperties;
import com.tek.core.properties.mail.TekMailProperties;
import com.tek.core.properties.swagger.TekSwaggerProperties;
import com.tek.core.properties.web.TekWebProperties;
import javax.validation.Valid;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


/**
 * Tek Core Module properties to be evaluated from application.yaml / application.properties files.
 *
 * @author MarcoPagan
 */
@ConfigurationProperties(prefix = TEK_CORE_PREFIX)
@Data
@Validated
public class TekCoreProperties {

  private TekWebProperties webConfiguration = new TekWebProperties();
  @Valid private TekLocaleProperties localeConfiguration = new TekLocaleProperties();
  private TekFileProperties fileConfiguration = new TekFileProperties();
  private TekMailProperties mailConfiguration = new TekMailProperties();
  private TekSwaggerProperties swaggerConfiguration = new TekSwaggerProperties();
}


