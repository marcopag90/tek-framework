package com.tek.jpa.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.domain.Author;
import com.tek.jpa.domain.Book.AuthorView;
import com.tek.jpa.service.mock.AuthorWritableDalService;
import com.tek.jpa.service.mock.BookWritableDalService;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = TekRestJpaApplication.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUtilsTest {

  @Autowired private AuthorWritableDalService authorService;
  @Autowired private BookWritableDalService bookService;

  private EntityUtils entityUtils;

  @BeforeAll
  @Test
  void setup() {
    Assertions.assertAll(
        () -> assertNotNull(authorService),
        () -> assertNotNull(bookService)
    );
    entityUtils = new EntityUtils(authorService.entityManager);
  }

  @Test
  @SneakyThrows
  void test_validate_path() {
    final var properties = Map.of(
        "name", "",
        "books", "",
        "books.author", "",
        "ratings", ""
    );
    for (String property : properties.keySet()) {
      entityUtils.validatePath(property, authorService.getEntityType(), null);
    }
  }

  @Test
  void test_validate_path_unknown_property() {
    final var path = "books.whatever";
    final var entityType = authorService.getEntityType();
    assertThrows(
        IllegalArgumentException.class,
        () -> entityUtils.validatePath(path, entityType, null)
    );
  }

  @Test
  void test_validate_path_access_denied() {
    Map<String, Serializable> properties = Map.of("id", 1L);
    final var view = Author.Views.UserView.class;
    for (String property : properties.keySet()) {
      assertThrows(
          AccessDeniedException.class,
          () -> entityUtils.validatePath(property, authorService.getEntityType(), view)
      );
    }
  }

  @Test
  void test_validate_path_access_denied_nested_property() {
    Map<String, Serializable> properties = Map.of(
        "author.id", 1L
    );
    assertThrows(
        AccessDeniedException.class,
        () -> {
          for (String property : properties.keySet()) {
            entityUtils.validatePath(property, bookService.getEntityType(), AuthorView.class);
          }
        }
    );
  }

  @Test
  void test_validate_path_nested_property_allowed() {
    Map<String, Serializable> properties = Map.of(
        "author.absolute", 1L
    );
    assertDoesNotThrow(
        () -> {
          for (String property : properties.keySet()) {
            entityUtils.validatePath(property, bookService.getEntityType(), AuthorView.class);
          }
        }
    );
  }
}