package com.tek.core.properties.file;


import com.tek.core.config.scheduler.TekSchedulerConfiguration;
import com.tek.core.properties.scheduler.TekSchedulerProperties;
import java.io.File;
import lombok.Data;

@Data
public class TmpProperties {

  private boolean enabled = false;

  /**
   * Tmp directory path
   */
  private File directoryPath;

  /**
   * Tmp directory cleans after (days)
   * <p> see {@link TekSchedulerConfiguration}
   */
  private Integer cleanAfter = 10;

  private String cron = TekSchedulerProperties.CRON_DAILY_MIDNIGHT;
}