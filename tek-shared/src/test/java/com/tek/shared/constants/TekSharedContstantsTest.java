package com.tek.shared.constants;

import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekSharedContstantsTest {

  @Test
  void test_default_values() {
    Assertions.assertEquals(TekSharedConstants.DEFAULT_LOCALE, Locale.forLanguageTag("en-US"));
  }

}
