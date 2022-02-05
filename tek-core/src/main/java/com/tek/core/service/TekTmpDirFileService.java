package com.tek.core.service;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_TMP_FILE_SERVICE;

import com.tek.core.config.directory.TekTmpDirConfiguration;
import com.tek.shared.io.TekFileUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

//TODO tests
@Service(TEK_CORE_TMP_FILE_SERVICE)
@ConditionalOnBean(TekTmpDirConfiguration.class)
public class TekTmpDirFileService {

  private final Logger log = LoggerFactory.getLogger(TekTmpDirFileService.class);

  @Autowired
  private TekTmpDirConfiguration tmpDirConfiguration;

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
  public File createInTmpDir(@NonNull String name) throws IOException {
    String directory = tmpDirectory + File.separator + LocalDate.now();
    return TekFileUtils.deepCreate(directory, name);
  }
}
