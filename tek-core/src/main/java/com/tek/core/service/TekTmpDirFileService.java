package com.tek.core.service;

import com.tek.core.config.directory.TekTmpDirConfiguration;
import com.tek.shared.io.TekFileUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

//TODO tests
@Service
@RequiredArgsConstructor
@Slf4j
public class TekTmpDirFileService {

  private final TekTmpDirConfiguration tmpDirConfiguration;

  private File tmpDirectory;

  @SuppressWarnings("unused")
  @PostConstruct
  private void init() {
    this.tmpDirectory = tmpDirConfiguration.tmpDirectory();
  }

  /**
   * Attempt to create a file inside the default application tmp directory.
   */
  @NonNull
  public String createInTmpDir(@NonNull String name) throws IOException {
    String directory = tmpDirectory + File.separator + LocalDate.now();
    return TekFileUtils.deepCreate(directory, name);
  }
}
