package com.tek.core.i18n;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_ACCEPT_HEADER_CONFIGURATION;
import static com.tek.core.constants.TekLocaleConstants.ACCEPT_HEADER;
import static com.tek.shared.constants.TekSharedConstants.DEFAULT_LOCALE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration(TEK_CORE_ACCEPT_HEADER_CONFIGURATION)
@ConditionalOnProperty(value = "tek.core.localeConfiguration.type", havingValue = ACCEPT_HEADER)
public class TekAcceptHeaderLocaleConfiguration {

  private final Logger log = LoggerFactory.getLogger(TekAcceptHeaderLocaleConfiguration.class);

  @Bean
  public AcceptHeaderLocaleResolver localeResolver() {
    final var resolver = new AcceptHeaderLocaleResolver();
    resolver.setDefaultLocale(DEFAULT_LOCALE);
    log.info("Exposing locale resolver: [{}]", AcceptHeaderLocaleResolver.class.getSimpleName());
    return resolver;
  }
}
