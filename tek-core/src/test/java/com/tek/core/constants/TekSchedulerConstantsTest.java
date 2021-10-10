package com.tek.core.constants;

import static com.tek.core.constants.TekSchedulerConstants.CRON_DAILY_MIDNIGHT;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekSchedulerConstantsTest {

  @Test
  void defaultValues() {
    Assertions.assertEquals("0 0 * * * *", CRON_DAILY_MIDNIGHT);
  }
}
