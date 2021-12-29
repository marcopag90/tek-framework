package com.tek.shared.io;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.lang.NonNull;

//TODO tests

/**
 * Utility to manage file and directories.
 *
 * @author MarcoPagan
 */
@Slf4j
public class TekFileUtils {

  private TekFileUtils() {
  }

  /**
   * Attempt to create a file. If the file has to be created inside a directory, the fully qualified
   * name must be provided.
   * <p>
   * Returns the newly created file path.
   */
  @NonNull
  public static String createFile(@NonNull String fileName) throws IOException {
    final var file = new File(fileName);
    boolean created = file.createNewFile();
    final var filePath = file.getPath();
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
  @NonNull
  public static File createDirectory(@NonNull String directory) {
    final var dir = new File(directory + File.separator);
    boolean created;
    if (!dir.isDirectory()) {
      log.debug(
          "Directory {} doesn't exist. Performing creation...", dir.getAbsolutePath()
      );
    }
    created = dir.mkdirs();
    if (created) {
      log.debug("Directory created :{} ", dir.getAbsolutePath());
      return dir;
    } else {
      log.debug("Directory {} already exists.", dir.getAbsolutePath());
    }
    return dir;
  }

  /**
   * Attempt to create a file inside the provided directory structure. If the directory structure
   * doesn't exist, attempts to create the directory structure first.
   * <p>
   * Returns the newly created file path.
   */
  @NonNull
  public static String deepCreate(
      @NonNull String directory,
      @NonNull String fileName
  ) throws IOException {
    final var directories = createDirectory(directory);
    return createFile(directories + File.separator + fileName);
  }

  /**
   * Deletes a directory recursively
   */
  public static void deepDelete(@NonNull File directory) {
    log.debug("Deleting directory: {}", directory.getAbsolutePath());
    try {
      FileUtils.deleteDirectory(directory);
      log.debug("Directory deleted: {}", directory.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
