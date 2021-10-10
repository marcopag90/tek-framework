package com.tek.core.service;

import com.tek.core.config.directory.TekBinaryDirConfiguration;
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
public class TekBinDirFileService {

  @NonNull private final TekBinaryDirConfiguration binaryDirConfiguration;
  @NonNull private final TekFileService service;

  private File binaryDirectory;

  @SuppressWarnings("unused")
  @PostConstruct
  private void init() {
    this.binaryDirectory = binaryDirConfiguration.binaryDirectory();
  }

  /**
   * Attempt to create a file inside the default application binary directory.
   */
  public String createInBinDir(String name) {
    String directory = binaryDirectory + File.separator + LocalDate.now();
    return service.deepCreate(directory, name);
  }
}
