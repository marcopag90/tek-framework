package com.tek.core.service;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

//TODO tests

/**
 * Utility service to manage file and directories.
 *
 * @author MarcoPagan
 */
@Service
@Slf4j
public class TekFileService {

  /**
   * Attempt to create a file. If the file has to be created inside a directory, the fully qualified
   * name must be provided.
   * <p>
   * Returns the newly created file path.
   */
  public String createFile(String fileName) {
    final var file = new File(fileName);
    boolean created;
    try {
      created = file.createNewFile();
    } catch (IOException e) {
      TekFileService.log.error("Could not create file: {}", file.getAbsolutePath());
      return null;
    }
    String filePath = file.getPath();
    if (created) {
      TekFileService.log.debug("File created at path: {}", file.getAbsolutePath());
    } else {
      TekFileService.log.debug("File {} already exists.", file.getAbsolutePath());
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
    final var dir = new File(directory + File.separator);
    boolean created;
    if (!dir.isDirectory()) {
      TekFileService.log.debug(
          "Directory {} doesn't exist. Performing creation...", dir.getAbsolutePath()
      );
    }
    try {
      created = dir.mkdirs();
      if (created) {
        TekFileService.log.debug("Directory created :{} ", dir.getAbsolutePath());
        return dir;
      } else {
        TekFileService.log.debug("Directory {} already exists.", dir.getAbsolutePath());
      }
    } catch (Exception e) {
      TekFileService.log.error("Could not create directory: {}", dir);
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
    TekFileService.log.debug("Deleting directory: {}", directory.getAbsolutePath());
    try {
      FileUtils.deleteDirectory(directory);
      TekFileService.log.debug("Directory deleted: {}", directory.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
