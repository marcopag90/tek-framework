package com.tek.core.properties.file;


import com.tek.core.properties.scheduler.TekSchedulerProperties;
import java.io.File;
import lombok.Data;

@Data
public class TmpProperties {

  /**
   * Tmp directory path
   */
  private File directory = new File("tmp");

  /**
   * Tmp directory cleans after (days)
   * <p> see {@link com.tek.core.task.TekTmpFileScheduler}
   */
  private Integer cleanAfter = 10;

  private String cron = TekSchedulerProperties.CRON_DAILY_MIDNIGHT;
}