package com.tek.jpa.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.domain.Author;
import com.tek.jpa.domain.Project.ProjectId;
import com.tek.jpa.domain.Store.Id;
import com.tek.jpa.service.mock.AuthorReadOnlyDalService;
import com.tek.jpa.service.mock.ProjectReadOnlyDalService;
import com.tek.jpa.service.mock.StoreReadOnlyDalService;
import com.tek.rest.shared.exception.EntityNotFoundException;
import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = TekRestJpaApplication.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class ReadOnlyDalServiceTest {

  @Autowired private AuthorReadOnlyDalService authorReadOnlyDalService;
  @Autowired private StoreReadOnlyDalService storeReadOnlyDalService;
  @Autowired private ProjectReadOnlyDalService proejctReadOnlyDalService;

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_entity_not_found_exception() {
    assertThrows(EntityNotFoundException.class, () -> authorReadOnlyDalService.findById(100));
  }

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_findById_with_user_view() {
    assertNull(authorReadOnlyDalService.findById(1).getId());
  }

  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_findById_with_developer_view() {
    assertNotNull(authorReadOnlyDalService.findById(1).getId());
  }

  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_findById_with_embedded_id() {
    assertNotNull(storeReadOnlyDalService.findById(new Id("Mediaworld", 1)));
  }

  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_findById_with_idClass() {
    assertNotNull(proejctReadOnlyDalService.findById(new ProjectId("TekFramework", "Java")));
  }

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_findAll_with_user_view() {
    var authors = authorReadOnlyDalService.findAll(null, PageRequest.of(0, 20));
    Predicate<Author> nullIdPredicate = a -> a.getId() == null;
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(nullIdPredicate))
    );
  }

  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_findAll_with_developer_view() {
    var authors = authorReadOnlyDalService.findAll(null, PageRequest.of(0, 20));
    Predicate<Author> notNullIdPredicate = a -> a.getId() != null;
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(notNullIdPredicate))
    );
  }

}
