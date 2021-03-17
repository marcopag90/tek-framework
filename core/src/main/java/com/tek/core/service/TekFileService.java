package com.tek.core.service;

import com.tek.core.config.TekDirConfiguration;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

/**
 * Service to manage file inside the application.
 *
 * @author MarcoPagan
 */
//TODO create uploadDirectory file, cron jobs for upload dir
@Service
@RequiredArgsConstructor
@Slf4j
public class TekFileService {

  @NonNull
  private final TekDirConfiguration dirConfiguration;

  private File tmpDirectory;
  private File binaryDirectory;

  @PostConstruct
  private void init() {
    this.tmpDirectory = dirConfiguration.tmpDirectory();
    this.binaryDirectory = dirConfiguration.binaryDirectory();
  }

  /**
   * Attempt to create a file. If the file has to be created inside a directory, the fully qualified
   * name must be provided.
   * <p>
   * Returns the newly created file path.
   */
  public String createFile(String fileName) {
    File file = new File(fileName);
    boolean created;
    try {
      created = file.createNewFile();
    } catch (IOException e) {
      log.error("Could not create file: {}", file.getAbsolutePath());
      return null;
    }
    String filePath = file.getPath();
    if (created) {
      log.debug("File created at path: {}", file.getAbsolutePath());
    } else {
      log.debug("File {} already exists.", file.getAbsolutePath());
    }
    return filePath;
  }

  /**
   * Attempt to create the given directory structure (including optional subdirectories).
   * <p>
   * Returns the directory created.
   * <p>
   * E.g: /myDir/mySubDir
   */
  public File createDirectory(String directory) {
    File dir = new File(directory + File.separator);
    boolean created;
    if (!dir.isDirectory()) {
      log.debug(
          "Directory {} doesn't exist. Performing creation...", dir.getAbsolutePath()
      );
    }
    try {
      created = dir.mkdirs();
      if (created) {
        log.debug("Directory created :{} ", dir.getAbsolutePath());
        return dir;
      } else {
        log.debug("Directory {} already exists.", dir.getAbsolutePath());
      }
    } catch (Exception e) {
      log.error("Could not create directory: {}", dir);
      return null;
    }
    return dir;
  }

  /**
   * Attempt to create a file inside the provided directory structure. If the directory structure
   * doesn't exist, attempts to create the directory structure first.
   * <p>
   * Returns the newly created file path.
   */
  public String deepCreate(String directory, String fileName) {
    File directories = createDirectory(directory);
    return createFile(directories + File.separator + fileName);
  }

  /**
   * Deletes a directory recursively
   */
  public void deepDelete(File directory) {
    log.debug("Deleting directory: {}", directory.getAbsolutePath());
    try {
      FileUtils.deleteDirectory(directory);
      log.debug("Directory deleted: {}", directory.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Attempt to create a file inside the default application tmp directory.
   */
  public String createInTmpDir(String name) {
    String directory = tmpDirectory + File.separator + LocalDate.now().toString();
    return deepCreate(directory, name);
  }
}
