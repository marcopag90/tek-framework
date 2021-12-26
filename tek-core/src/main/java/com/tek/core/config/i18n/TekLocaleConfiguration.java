package com.tek.core.config.i18n;

import static com.tek.core.constants.TekCoreBeanConstants.TEK_LOCALE_CHANGE_INTERCEPTOR_BEAN;
import static com.tek.core.constants.TekCoreBeanConstants.TEK_LOCALE_RESOLVER_BEAN;

import com.tek.core.TekCoreAutoConfig;
import com.tek.core.properties.TekCoreProperties;
import com.tek.core.properties.i18n.TekLocaleProperties;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Configuration to determine:
 * <p>
 * {@link LocaleResolver}: supported types are qualifed by {@link TekLocaleProperties.TekLocaleType}
 * <p>
 * {@link LocaleChangeInterceptor}: trigger parameter to detect the locale change
 *
 * @author MarcoPagan
 */
@Configuration
@ConditionalOnClass(TekCoreAutoConfig.class)
@Slf4j
public record TekLocaleConfiguration(TekCoreProperties coreProperties) implements WebMvcConfigurer {

  public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    log.info("Registering locale resolver: [{}]", localeResolver());
    registry.addInterceptor(localeChangeInterceptor());
  }

  @Bean(TEK_LOCALE_RESOLVER_BEAN)
  public LocaleResolver localeResolver() {
    TekLocaleProperties localeProperties = coreProperties.getLocaleConfiguration();
    TekLocaleProperties.TekLocaleType type = localeProperties.getType();
    if (type == TekLocaleProperties.TekLocaleType.SESSION) {
      final var sessionLocaleResolver = new SessionLocaleResolver();
      sessionLocaleResolver.setDefaultLocale(DEFAULT_LOCALE);
      return sessionLocaleResolver;
    } else if (type == TekLocaleProperties.TekLocaleType.COOKIE) {
      final var cookieLocaleResolver = new CookieLocaleResolver();
      cookieLocaleResolver.setDefaultLocale(DEFAULT_LOCALE);
      cookieLocaleResolver.setCookieName(localeProperties.getCookieName());
      cookieLocaleResolver.setCookieMaxAge(localeProperties.getCookieMaxAge());
      return cookieLocaleResolver;
    } else {
      throw new UnsupportedOperationException("type: [" + type + "] not supported.");
    }
  }

  /**
   * Interceptor that will switch to a new <i>locale</i> based on the value of the <i>lang</i>
   * parameter appended inside the Url request as a {@link org.springframework.web.bind.annotation.RequestParam}
   */
  @Bean(TEK_LOCALE_CHANGE_INTERCEPTOR_BEAN)
  public LocaleChangeInterceptor localeChangeInterceptor() {
    final var localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setHttpMethods(HttpMethod.POST.name());
    return localeChangeInterceptor;
  }
}
