package com.tek.jpa.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.service.AuthorWritableDalService;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = TekRestJpaApplication.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntityManagerUtilsTest {

  @Autowired
  private AuthorWritableDalService dalService;

  @Test
  void test_validate_path() {
    final var utils = new EntityManagerUtils(dalService.entityManager());
    final var properties = List.of("name", "books", "books.author", "ratings");
    properties.forEach(
        prop -> utils.validatePath(prop, dalService.getEntityType())
    );
  }

  @Test
  void test_validate_path_unknown_property() {
    final var utils = new EntityManagerUtils(dalService.entityManager());
    final var path = "books.whatever";
    final var entityType = dalService.getEntityType();
    assertThrows(IllegalArgumentException.class, () -> utils.validatePath(path, entityType));
  }
}