package com.tek.shared.io;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * Utility to handle file and directories structures.
 *
 * @author MarcoPagan
 */
record TekFileUtils() {

  private static final Logger log = LoggerFactory.getLogger(TekFileUtils.class);

  /**
   * Attempts to create a file. If the file has to be created inside a directory, the fully
   * qualified name must be provided.
   */
  @NonNull
  public static File createFile(@NonNull String fileName) throws IOException {
    Objects.requireNonNull(fileName);
    final var file = new File(fileName);
    boolean created = file.createNewFile();
    if (created) {
      log.debug("File created at path {}", file.getAbsolutePath());
    } else {
      log.debug("File {} already exists.", file.getAbsolutePath());
    }
    return file;
  }

  /**
   * Attempts to create the given directory structure (including optional subdirectories).
   */
  @NonNull
  public static File createDirectory(@NonNull String directory) {
    Objects.requireNonNull(directory);
    final var dir = new File(directory + File.separator);
    boolean created;
    if (!dir.isDirectory()) {
      log.debug(
          "Directory {} doesn't exist. Performing creation...", dir.getAbsolutePath()
      );
    }
    created = dir.mkdirs();
    if (created) {
      log.debug("Directory created {} ", dir.getAbsolutePath());
      return dir;
    } else {
      log.debug("Directory {} already exists.", dir.getAbsolutePath());
    }
    return dir;
  }

  /**
   * Attempts to create a file inside the provided directory structure. If the directory structure
   * doesn't exist, attempts to create the directory structure first.
   */
  @NonNull
  public static File deepCreate(
      @NonNull String directory,
      @NonNull String fileName
  ) throws IOException {
    Objects.requireNonNull(directory);
    Objects.requireNonNull(fileName);
    final var directories = createDirectory(directory);
    return createFile(directories + File.separator + fileName);
  }

  /**
   * Deletes a directory recursively.
   */
  public static void deepDelete(@NonNull File directory) throws IOException {
    Objects.requireNonNull(directory);
    log.debug("Deleting directory {}", directory.getAbsolutePath());
    FileUtils.deleteDirectory(directory);
    log.debug("Directory deleted {}", directory.getAbsolutePath());
  }
}
