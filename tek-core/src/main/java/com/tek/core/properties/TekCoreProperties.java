package com.tek.core.properties;

import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PREFIX;

import com.tek.core.properties.i18n.TekLocaleProperties;
import com.tek.core.properties.mail.TekMailProperties;
import com.tek.core.properties.swagger.TekSwaggerProperties;
import com.tek.core.properties.web.TekWebProperties;
import javax.validation.Valid;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


/**
 * Tek Core Module properties to be evaluated from application.yaml / application.properties files.
 *
 * @author MarcoPagan
 */
@Configuration
@ConfigurationProperties(prefix = TEK_CORE_PREFIX)
@Validated
public class TekCoreProperties {

  private TekWebProperties webConfiguration = new TekWebProperties();
  @Valid
  private TekLocaleProperties localeConfiguration = new TekLocaleProperties();
  private TekMailProperties mailConfiguration = new TekMailProperties();
  private TekSwaggerProperties swaggerConfiguration = new TekSwaggerProperties();

  public TekWebProperties getWebConfiguration() {
    return webConfiguration;
  }

  public void setWebConfiguration(TekWebProperties webConfiguration) {
    this.webConfiguration = webConfiguration;
  }

  public TekLocaleProperties getLocaleConfiguration() {
    return localeConfiguration;
  }

  public void setLocaleConfiguration(TekLocaleProperties localeConfiguration) {
    this.localeConfiguration = localeConfiguration;
  }

  public TekMailProperties getMailConfiguration() {
    return mailConfiguration;
  }

  public void setMailConfiguration(TekMailProperties mailConfiguration) {
    this.mailConfiguration = mailConfiguration;
  }

  public TekSwaggerProperties getSwaggerConfiguration() {
    return swaggerConfiguration;
  }

  public void setSwaggerConfiguration(TekSwaggerProperties swaggerConfiguration) {
    this.swaggerConfiguration = swaggerConfiguration;
  }
}


