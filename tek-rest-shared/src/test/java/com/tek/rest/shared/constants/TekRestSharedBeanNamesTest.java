package com.tek.rest.shared.constants;


import static com.tek.rest.shared.constants.TekRestSharedBeanNames.TEK_REST_SHARED_CONFIGURATION;
import static com.tek.rest.shared.constants.TekRestSharedBeanNames.TEK_REST_SHARED_GUAVA_MODULE;
import static com.tek.rest.shared.constants.TekRestSharedBeanNames.TEK_REST_SHARED_OPEN_API;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekRestSharedBeanNamesTest {

  @Test
  void testConstantValues() {
    Assertions.assertAll(
        () -> assertEquals(
            "com.tek.rest.shared.config.TekRestSharedConfiguration",
            TEK_REST_SHARED_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.rest.shared.config.TekRestSharedConfiguration.guavaModule",
            TEK_REST_SHARED_GUAVA_MODULE
        ),
        () -> assertEquals(
            "com.tek.rest.shared.config.TekRestSharedConfiguration.tekApi",
            TEK_REST_SHARED_OPEN_API
        )
    );
  }
}
