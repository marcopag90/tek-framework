package com.tek.core.properties.file;


import com.tek.core.config.scheduler.TekBinDirSchedulerConfiguration;
import com.tek.core.constants.TekSchedulerConstants;
import java.io.File;

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

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isSchedulerEnabled() {
    return schedulerEnabled;
  }

  public void setSchedulerEnabled(boolean schedulerEnabled) {
    this.schedulerEnabled = schedulerEnabled;
  }

  public File getDirectoryPath() {
    return directoryPath;
  }

  public void setDirectoryPath(File directoryPath) {
    this.directoryPath = directoryPath;
  }

  public Integer getCleanAfter() {
    return cleanAfter;
  }

  public void setCleanAfter(Integer cleanAfter) {
    this.cleanAfter = cleanAfter;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }
}