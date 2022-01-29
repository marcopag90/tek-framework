package com.tek.core.config.i18n;

import static com.tek.core.properties.i18n.TekLocaleConstants.ACCEPT_HEADER;
import static com.tek.shared.constants.TekSharedConstants.DEFAULT_LOCALE;

import com.tek.core.TekCoreAutoConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@ConditionalOnClass(TekCoreAutoConfig.class)
@ConditionalOnProperty(value = "tek.core.localeConfiguration.type", havingValue = ACCEPT_HEADER)
@Slf4j
public class TekAcceptHeaderLocaleConfiguration {

  @Bean
  public AcceptHeaderLocaleResolver localeResolver() {
    final var resolver = new AcceptHeaderLocaleResolver();
    resolver.setDefaultLocale(DEFAULT_LOCALE);
    log.info("Exposing locale resolver: [{}]", AcceptHeaderLocaleResolver.class.getSimpleName());
    return resolver;
  }
}
