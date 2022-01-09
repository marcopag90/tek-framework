package com.tek.shared.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class TekFileUtilsTest {

  private final List<File> toBeRemovedFiles = new ArrayList<>();

  @Test
  @Order(1)
  void test_createFile() throws IOException {
    final var file = TekFileUtils.createFile("myFile.txt");
    toBeRemovedFiles.add(file);
    Assertions.assertNotNull(file);
  }

  @Test
  @Order(2)
  void test_createDirectory() {
    final var dir = TekFileUtils.createDirectory("myDir");
    toBeRemovedFiles.add(dir);
    Assertions.assertNotNull(dir);
  }

  @Test
  @Order(3)
  void test_deepCreate() throws IOException {
    final var dir = TekFileUtils.createDirectory("anotherDir");
    final var file = TekFileUtils.deepCreate("anotherDir", "myFile.txt");
    toBeRemovedFiles.add(dir);
    toBeRemovedFiles.add(file);
    Assertions.assertNotNull(file);
  }

  @Test
  @Order(4)
  void test_deepDelete() throws IOException {
    final var dir = TekFileUtils.createDirectory("myFileDir");
    TekFileUtils.deepCreate("myFileDir", "myFile.csv");
    TekFileUtils.deepDelete(dir);
    Assertions.assertFalse(dir.exists());
  }

  @AfterAll
  void clean() {
    toBeRemovedFiles.forEach(File::deleteOnExit);
  }

}
