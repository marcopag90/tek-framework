package com.tek.core.task;

import static com.tek.core.TekCoreConstants.TEK_CORE;

import com.tek.core.TekCoreProperties;
import com.tek.core.service.TekFileService;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler to clean directory where unuseful file are stored.
 * <p>
 * Scheduler is executed synchronously, so if a scheduled thread is still running, the next one will
 * be on queue, waiting for the previous thread to finish its execution.
 */
@Component
@ConditionalOnProperty(prefix = TEK_CORE, name = "scheduler.active", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class TekTmpFileScheduler {

  @NonNull
  private final TekCoreProperties coreProperties;
  @NonNull
  private final TekFileService fileService;

  private File directory;
  private Integer cleanAfter;

  @PostConstruct
  private void init() {
    this.directory = coreProperties.getFile().getTmp().getDirectory();
    this.cleanAfter = coreProperties.getFile().getTmp().getCleanAfter();
  }

  @Scheduled(cron = "${tek.core.file.tmp.cron}", zone = "${spring.jackson.time-zone}")
  public void cleanTmpDirectory() throws InterruptedException {
    val today = LocalDate.now();
    log.info("Today date: {}", today);
    var start = today.minusDays(cleanAfter);
    log.info("cleanAfter parameter: {} days, starting date is {}", cleanAfter, start);

    if (!directory.exists()) {
      log.warn("Directory {} doesn't exist or has been deleted", directory.getAbsolutePath());
    }
    while (start.isBefore(today)) {
      val dir = Paths.get(directory + File.separator + start).toFile();
      if (dir.exists()) {
        log.info("Performing clean of directory {}...", dir.getAbsolutePath());
        fileService.deepDelete(dir);
        log.info("Directory {} successfully cleaned", dir.getAbsolutePath());
      }
      start = start.plusDays(1);
    }
  }
}
