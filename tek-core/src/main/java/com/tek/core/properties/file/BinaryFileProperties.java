package com.tek.core.properties.file;

import com.tek.core.config.scheduler.TekBinDirSchedulerConfiguration;
import com.tek.core.constants.TekSchedulerConstants;
import java.io.File;
import lombok.Data;

@Data
public class BinaryFileProperties {

  /**
   * Triggers {@link com.tek.core.config.directory.TekBinaryDirConfiguration} activation.
   */
  private boolean enabled;

  private boolean schedulerEnabled;

  private File directoryPath;

  /**
   * Tmp directory cleans after (days)
   * <p> see {@link TekBinDirSchedulerConfiguration}
   */
  private Integer cleanAfter = 10;

  /**
   * Upload directory cleans after (days)
   */
  private String cron = TekSchedulerConstants.CRON_DAILY_MIDNIGHT;
}
