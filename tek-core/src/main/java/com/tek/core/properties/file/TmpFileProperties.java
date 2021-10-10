package com.tek.core.properties.file;


import com.tek.core.config.scheduler.TekBinDirSchedulerConfiguration;
import com.tek.core.constants.TekSchedulerConstants;
import java.io.File;
import lombok.Data;

@Data
public class TmpFileProperties {

  /**
   * Triggers {@link com.tek.core.config.directory.TekTmpDirConfiguration} activation.
   */
  private boolean enabled;

  private boolean schedulerEnabled;

  /**
   * Tmp directory path
   */
  private File directoryPath;

  /**
   * Tmp directory cleans after (days)
   * <p> see {@link TekBinDirSchedulerConfiguration}
   */
  private Integer cleanAfter = 10;

  private String cron = TekSchedulerConstants.CRON_DAILY_MIDNIGHT;
}