package com.tek.core.service;

import com.tek.core.config.directory.TekTmpDirConfiguration;
import java.io.File;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//TODO tests
@Service
@RequiredArgsConstructor
@Slf4j
public class TekTmpDirFileService {

  @NonNull private final TekTmpDirConfiguration tmpDirConfiguration;
  @NonNull private final TekFileService service;

  private File tmpDirectory;

  @SuppressWarnings("unused")
  @PostConstruct
  private void init() {
    this.tmpDirectory = tmpDirConfiguration.tmpDirectory();
  }

  /**
   * Attempt to create a file inside the default application tmp directory.
   */
  public String createInTmpDir(String name) {
    String directory = tmpDirectory + File.separator + LocalDate.now();
    return service.deepCreate(directory, name);
  }
}
