package com.tek.core.properties.file;

import com.tek.core.properties.TekSchedulerProperties;
import lombok.Data;

import java.io.File;

@Data
public class BinaryProperties {

  private File directory = new File("upload");

  /**
   * Upload directory cleans after (days) //TODO see binary scheduler
   */
  private String cron = TekSchedulerProperties.CRON_DAILY_MIDNIGHT;
}
