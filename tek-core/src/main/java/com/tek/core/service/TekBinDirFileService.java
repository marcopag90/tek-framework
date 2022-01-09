package com.tek.core.service;

import com.tek.core.config.directory.TekBinaryDirConfiguration;
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
public class TekBinDirFileService {

  private final TekBinaryDirConfiguration binaryDirConfiguration;

  private File binaryDirectory;

  @SuppressWarnings("unused")
  @PostConstruct
  private void init() {
    this.binaryDirectory = binaryDirConfiguration.binaryDirectory();
  }

  /**
   * Attempt to create a file inside the default application binary directory.
   */
  @NonNull
  public File createInBinDir(@NonNull String name) throws IOException {
    String directory = binaryDirectory + File.separator + LocalDate.now();
    return TekFileUtils.deepCreate(directory, name);
  }
}
