package com.tek.core;

import static com.tek.core.TekCoreConstants.GIT_PROPERTIES;
import static com.tek.core.TekCoreConstants.TEK_CORE;
import static com.tek.core.TekCoreConstants.TEK_CORE_CONFIGURATION;
import static com.tek.core.TekCoreConstants.TEK_CORE_MESSAGE_BUNDLE;
import static com.tek.core.TekCoreConstants.TEK_CORE_MESSAGE_SOURCE;
import static com.tek.core.TekCoreConstants.TEK_DATE_PATH;
import static com.tek.core.TekCoreConstants.TEK_LOCALE_PATH;
import static com.tek.core.TekCoreConstants.TEK_PACKAGES_TO_SCAN;
import static com.tek.core.TekCoreConstants.TEK_SWAGGER_API_INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekCoreConstantsTest {

  @Test
  void testConstantValues() {
    Assertions.assertAll(
        () -> assertEquals("com.tek", TEK_PACKAGES_TO_SCAN),
        () -> assertEquals("TekCoreConfiguration", TEK_CORE_CONFIGURATION),
        () -> assertEquals("classpath:/i18n/core_messages", TEK_CORE_MESSAGE_BUNDLE),
        () -> assertEquals("com.tek.core.messageSource",TEK_CORE_MESSAGE_SOURCE),
        () -> assertEquals("tek.core",TEK_CORE),
        () -> assertEquals("/locale",TEK_LOCALE_PATH),
        () -> assertEquals("/dateformat",TEK_DATE_PATH),
        () -> assertEquals("tek.swagger.api.info",TEK_SWAGGER_API_INFO),
        () -> assertEquals("git.properties",GIT_PROPERTIES)
    );
  }
}
