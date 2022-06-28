package com.tek.core.constants;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_ACCEPT_HEADER_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_COOKIE_LOCALE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_COOKIE_LOCALE_RESOLVER;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_LOCALE_CHANGE_INTERCEPTOR;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_RESOURCE_BUNDLE;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_RESOURCE_BUNDLE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SESSION_LOCALE_CONFIGURATION;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekCoreBeanNamesTest {

  @Test
  @SuppressWarnings("squid:S5961")
  void testConstantValues() {
    Assertions.assertAll(
        () -> assertEquals(
            "com.tek.core.config.i18n.localeChangeInterceptor",
            TEK_CORE_LOCALE_CHANGE_INTERCEPTOR
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekAcceptHeaderLocaleConfiguration",
            TEK_CORE_ACCEPT_HEADER_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekCookieLocaleConfiguration",
            TEK_CORE_COOKIE_LOCALE_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekCookieLocaleConfiguration.localeResolver",
            TEK_CORE_COOKIE_LOCALE_RESOLVER
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekSessionLocaleConfiguration",
            TEK_CORE_SESSION_LOCALE_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekResourceBundleConfiguration",
            TEK_CORE_RESOURCE_BUNDLE_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekResourceBundleConfiguration.tekMessageSource",
            TEK_CORE_RESOURCE_BUNDLE
        )
    );
  }
}
