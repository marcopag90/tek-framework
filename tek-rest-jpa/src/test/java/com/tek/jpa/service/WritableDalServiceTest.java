package com.tek.jpa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.domain.Author;
import com.tek.jpa.service.mock.AuthorWritableDalService;
import com.tek.jpa.service.mock.EmployeeDalService;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

@SpringBootTest(classes = TekRestJpaApplication.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
class WritableDalServiceTest {

  @Autowired private AuthorWritableDalService authorDalService;
  @Autowired private EmployeeDalService employeeDalService;

  @BeforeAll
  @Test
  void test_setup() {
    Assertions.assertAll(
        () -> assertNotNull(authorDalService),
        () -> assertNotNull(employeeDalService)
    );
  }

  //------------------------------------- Create methods -------------------------------------------

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER"}
  )
  void test_method_argument_not_valid_exception() {
    assertThrows(MethodArgumentNotValidException.class,
        () -> authorDalService.create(new Author()));
  }

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER"}
  )
  void test_create_with_user_view() throws MethodArgumentNotValidException {
    var author = Author.builder()
        .name("marco")
        .surname("pagan")
        .build();
    var createdAuthor = authorDalService.create(author);
    Assertions.assertAll(
        () -> assertEquals(author.getName(), createdAuthor.getName()),
        () -> assertEquals(author.getSurname(), createdAuthor.getSurname()),
        () -> assertNull(createdAuthor.getId())
    );
  }

  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER"}
  )
  void test_create_with_developer_view() throws MethodArgumentNotValidException {
    var author = Author.builder()
        .name("marco")
        .surname("pagan")
        .build();
    var createdAuthor = authorDalService.create(author);
    Assertions.assertAll(
        () -> assertEquals(author.getName(), createdAuthor.getName()),
        () -> assertEquals(author.getSurname(), createdAuthor.getSurname()),
        () -> assertNotNull(createdAuthor.getId())
    );
  }

  //------------------------------------- Update methods -------------------------------------------
  /*
  1) entityUtils.validatePath(property, entityType, applyView());
    1.1) unknown property
    1.2) forbidden property
  2) entityType.hasVersionAttribute()
    2.1) version not provided
    2.2) version mismatch
  3) findById(id) - entity not found
  4) validatorAdapter.validate(entity, result);
  5) update finalization
    5.1) basic type
    5.2) entity type
  */

  //1.1
  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER"}
  )
  void test_update_path_not_valid_unknown_property() {
    Map<String, Serializable> properties = Map.of("surname", "Pagan");
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> employeeDalService.update(1L, properties, null)
    );
  }

  //1.2
  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER"}
  )
  void test_update_path_not_valid_forbidden_property() {
    Map<String, Serializable> properties = Map.of("createdAt", Instant.now());
    Assertions.assertThrows(
        AccessDeniedException.class,
        () -> employeeDalService.update(1L, properties, null)
    );
  }

  //2.1
  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER"}
  )
  void test_version_not_provided() {
    Assertions.assertThrows(
        MethodArgumentNotValidException.class,
        () -> employeeDalService.update(1L, Map.of(), null)
    );
  }

  //2.2
  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER"}
  )
  void test_version_mismatch() {
    Assertions.assertThrows(
        MethodArgumentNotValidException.class,
        () -> employeeDalService.update(1L, Map.of(), 2L)
    );
  }

  //3
  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER"}
  )
  void test_entity_not_found() {
    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> employeeDalService.update(100L, Map.of(), 1L)
    );
  }

  //4
  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER"}
  )
  void test_entity_not_valid() {
    Map<String, Serializable> properties = new HashMap<>();
    properties.put("name", null);
    Assertions.assertThrows(
        MethodArgumentNotValidException.class,
        () -> employeeDalService.update(1L, properties, 1L)
    );
  }

  //5.1
  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER"}
  )
  void test_update_with_basic_type()
      throws NoSuchFieldException, EntityNotFoundException, MethodArgumentNotValidException {
    Map<String, Serializable> properties = new HashMap<>();
    properties.put("name", "Marco Pagan");
    properties.put("income", "36000.50");
    properties.put("lastContract", null);
    final var update = employeeDalService.update(1L, properties, 1L);
    Assertions.assertAll(
        () -> assertNotNull(update),
        () -> assertEquals("Marco Pagan", update.getName()),
        () -> assertEquals(new BigDecimal("36000.50"), update.getIncome()),
        () -> assertNull(update.getLastContract())
    );
  }

  //5.2
  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"COMPANY"}
  )
  void test_update_with_entity_type()
      throws EntityNotFoundException, NoSuchFieldException, MethodArgumentNotValidException {
    Map<String, Serializable> properties = new HashMap<>();
    properties.put("company.id", 2);
    final var update = employeeDalService.update(1L, properties, 1L);
    Assertions.assertAll(
        () -> assertNotNull(update),
        () -> assertEquals(2, update.getCompany().getId())
    );
  }

  //------------------------------------- Delete methods -------------------------------------------
  @Test
  @WithMockUser(value = "ADMIN")
  void test_delete() throws EntityNotFoundException {
    assertNotNull(authorDalService.findById(1));
    authorDalService.deleteById(1);
    assertThrows(EntityNotFoundException.class, () -> authorDalService.findById(1));
  }
}
