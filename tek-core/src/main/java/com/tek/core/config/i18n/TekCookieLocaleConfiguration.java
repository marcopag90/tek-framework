package com.tek.core.config.i18n;

import static com.tek.core.properties.i18n.TekLocaleConstants.COOKIE;
import static com.tek.shared.constants.TekSharedConstants.DEFAULT_LOCALE;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Configuration
@ConditionalOnClass(TekCoreAutoConfig.class)
@ConditionalOnProperty(value = "tek.core.localeConfiguration.type", havingValue = COOKIE)
@Slf4j
public class TekCookieLocaleConfiguration {

  @Bean
  public CookieLocaleResolver localeResolver(TekCoreProperties coreProperties) {
    final var properties = coreProperties.getLocaleConfiguration();
    final var resolver = new CookieLocaleResolver();
    resolver.setDefaultLocale(DEFAULT_LOCALE);
    resolver.setCookieName(properties.getCookieName());
    resolver.setCookieMaxAge(properties.getCookieMaxAge());
    log.info("Exposing locale resolver: [{}]", CookieLocaleResolver.class.getSimpleName());
    return resolver;
  }
}
