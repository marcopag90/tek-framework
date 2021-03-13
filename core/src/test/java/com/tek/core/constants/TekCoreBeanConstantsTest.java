package com.tek.core.constants;

import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_FILE_TIMESTAMP_BEAN;
import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_MESSAGE_SOURCE_BEAN;
import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_TIMESTAMP_BEAN;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekCoreBeanConstantsTest {

  @Test
  void testConstantValues() {
    Assertions.assertAll(
        () -> assertEquals("TekCoreConfiguration", TEK_CORE_CONFIGURATION),
        () -> assertEquals("com.tek.core.messageSource", TEK_CORE_MESSAGE_SOURCE_BEAN),
        () -> assertEquals("TekCoreTimestampDateFormat", TEK_CORE_TIMESTAMP_BEAN),
        () -> assertEquals("TekCoreFiletimestampFormat", TEK_CORE_FILE_TIMESTAMP_BEAN)
    );
  }
}
