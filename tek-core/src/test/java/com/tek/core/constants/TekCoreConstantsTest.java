package com.tek.core.constants;

import static com.tek.core.constants.TekCoreConstants.GIT_PROPERTIES;
import static com.tek.core.constants.TekCoreConstants.TEK_CORE_MESSAGE_BUNDLE;
import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PACKAGES_TO_SCAN;
import static com.tek.core.constants.TekCoreConstants.TEK_CORE_PREFIX;
import static com.tek.core.constants.TekCoreConstants.TEK_DATE_PATH;
import static com.tek.core.constants.TekCoreConstants.TEK_LOCALE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekCoreConstantsTest {

  @Test
  void testConstantValues() {
    Assertions.assertAll(
        () -> assertEquals("com.tek.core", TEK_CORE_PACKAGES_TO_SCAN),
        () -> assertEquals("classpath:/i18n/core_messages", TEK_CORE_MESSAGE_BUNDLE),
        () -> assertEquals("tek.core", TEK_CORE_PREFIX),
        () -> assertEquals("/locale", TEK_LOCALE_PATH),
        () -> assertEquals("/dateformat", TEK_DATE_PATH),
        () -> assertEquals("git.properties", GIT_PROPERTIES)
    );
  }
}
