package com.tek.core.properties.file;

import com.tek.core.config.scheduler.TekSchedulerConfiguration;
import com.tek.core.properties.scheduler.TekSchedulerProperties;
import java.io.File;
import lombok.Data;

@Data
public class BinaryProperties {

  private boolean enabled = false;

  private File directoryPath;

  /**
   * Tmp directory cleans after (days)
   * <p> see {@link TekSchedulerConfiguration}
   */
  private Integer cleanAfter = 10;

  /**
   * Upload directory cleans after (days) //TODO see binary scheduler
   */
  private String cron = TekSchedulerProperties.CRON_DAILY_MIDNIGHT;
}
