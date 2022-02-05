package com.tek.core.config.i18n;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_RESOURCE_BUNDLE;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_RESOURCE_BUNDLE_CONFIGURATION;
import static com.tek.core.constants.TekCoreConstants.TEK_CORE_MESSAGE_BUNDLE;
import static com.tek.shared.constants.TekSharedConstants.DEFAULT_LOCALE;

import com.tek.core.TekCoreAutoConfig;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration(TEK_CORE_RESOURCE_BUNDLE_CONFIGURATION)
@ConditionalOnClass(TekCoreAutoConfig.class)
public class TekResourceBundleConfiguration {

  /**
   * If both an unsupported language tag or an unmapped language property is found, fallbacks to the
   * default language tag/property.
   */
  @Bean(name = TEK_CORE_RESOURCE_BUNDLE)
  public MessageSource tekMessageSource() {
    final var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename(TEK_CORE_MESSAGE_BUNDLE);
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    messageSource.setDefaultLocale(DEFAULT_LOCALE);
    return messageSource;
  }

}
