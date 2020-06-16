package com.tek.core.conf.i18n;

import com.tek.core.TekCoreProperties;
import com.tek.core.prop.i18n.TekLocaleProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuration to determine:
 * <p>
 * {@link LocaleResolver}: supported types are qualifed by
 * {@link TekLocaleProperties.TekLocaleType}
 * <p>
 * {@link LocaleChangeInterceptor}: trigger parameter to detect the locale change
 *
 * @author MarcoPagan
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class TekLocaleConfiguration implements WebMvcConfigurer {

    @NonNull private final TekCoreProperties coreProperties;

    public final Locale defaultLocale = Locale.ENGLISH;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering locale resolver: [{}]", localeResolver());
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleResolver localeResolver() {
        TekLocaleProperties localeProperties = coreProperties.getLocale();
        TekLocaleProperties.TekLocaleType type = localeProperties.getType();

        if (type == TekLocaleProperties.TekLocaleType.SESSION) {
            SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
            sessionLocaleResolver.setDefaultLocale(defaultLocale);
            return sessionLocaleResolver;
        } else if (type == TekLocaleProperties.TekLocaleType.COOKIE) {
            CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
            cookieLocaleResolver.setDefaultLocale(defaultLocale);
            cookieLocaleResolver.setCookieName(localeProperties.getCookieName());
            cookieLocaleResolver.setCookieMaxAge(localeProperties.getCookieMaxAge());
            return cookieLocaleResolver;
        } else {
            throw new UnsupportedOperationException("type: [" + type + "] not supported.");
        }
    }

    /**
     * Interceptor that will switch to a new <i>locale</i> based on the value of the <i>lang</i>
     * parameter appended inside the Url request as a
     * {@link org.springframework.web.bind.annotation.RequestParam}
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setHttpMethods(HttpMethod.POST.name());
        return localeChangeInterceptor;
    }
}
