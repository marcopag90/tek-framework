package com.tek.core.i18n;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_LOCALE_CHANGE_INTERCEPTOR;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SESSION_LOCALE_CONFIGURATION;
import static com.tek.core.constants.TekLocaleConstants.SESSION;
import static com.tek.shared.constants.TekSharedConstants.DEFAULT_LOCALE;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration(TEK_CORE_SESSION_LOCALE_CONFIGURATION)
@ConditionalOnClass(TekCoreAutoConfig.class)
@ConditionalOnProperty(value = "tek.core.localeConfiguration.type", havingValue = SESSION)
public class TekSessionLocaleConfiguration implements WebMvcConfigurer {

  private final Logger log = LoggerFactory.getLogger(TekSessionLocaleConfiguration.class);

  @Autowired
  private TekCoreProperties coreProperties;

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    log.info("Registering locale interceptor: [{}]", localeChangeInterceptor());
    registry.addInterceptor(localeChangeInterceptor());
  }

  @Bean
  public SessionLocaleResolver localeResolver() {
    final var resolver = new SessionLocaleResolver();
    resolver.setDefaultLocale(DEFAULT_LOCALE);
    log.info("Exposing locale resolver: [{}]", SessionLocaleResolver.class.getSimpleName());
    return resolver;
  }

  /**
   * Interceptor that will switch to a new <i>locale</i> based on the value of the <i>locale</i>
   * parameter appended inside the Url request as a {@link org.springframework.web.bind.annotation.RequestParam}
   */
  @Bean(TEK_CORE_LOCALE_CHANGE_INTERCEPTOR)
  public LocaleChangeInterceptor localeChangeInterceptor() {
    final var properties = coreProperties.getLocaleConfiguration();
    final var interceptor = new LocaleChangeInterceptor();
    interceptor.setHttpMethods(HttpMethod.POST.name());
    interceptor.setParamName(properties.getPostParameterName());
    log.info("Exposing locale interceptor: [{}]", LocaleChangeInterceptor.class.getSimpleName());
    return interceptor;
  }
}
