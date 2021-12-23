package com.tek.jpa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.domain.Author;
import com.tek.jpa.service.mock.AuthorWritableDalService;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

@SpringBootTest(classes = TekRestJpaApplication.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class WritableDalServiceTest {

  @Autowired
  private AuthorWritableDalService dalService;

  //------------------------------------- Create methods -------------------------------------------

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_method_argument_not_valid_exception() {
    assertThrows(MethodArgumentNotValidException.class, () -> dalService.create(new Author()));
  }

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_create_with_user_view() {
    var author = Author.builder()
        .name("marco")
        .surname("pagan")
        .build();
    var createdAuthor = dalService.create(author);
    Assertions.assertAll(
        () -> assertEquals(author.getName(), createdAuthor.getName()),
        () -> assertEquals(author.getSurname(), createdAuthor.getSurname()),
        () -> assertNull(createdAuthor.getId())
    );
  }

  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_create_with_developer_view() {
    var author = Author.builder()
        .name("marco")
        .surname("pagan")
        .build();
    var createdAuthor = dalService.create(author);
    Assertions.assertAll(
        () -> assertEquals(author.getName(), createdAuthor.getName()),
        () -> assertEquals(author.getSurname(), createdAuthor.getSurname()),
        () -> assertNotNull(createdAuthor.getId())
    );
  }

  //------------------------------------- Read methods ---------------------------------------------

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_entity_not_found_exception() {
    assertThrows(EntityNotFoundException.class, () -> dalService.findById(100));
  }

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_findById_with_user_view() {
    assertNull(dalService.findById(1).getId());
  }

  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_findById_with_developer_view() {
    assertNotNull(dalService.findById(1).getId());
  }

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_findAll_with_user_view() {
    var authors = dalService.findAll(null, PageRequest.of(0, 20));
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
    var authors = dalService.findAll(null, PageRequest.of(0, 20));
    Predicate<Author> notNullIdPredicate = a -> a.getId() != null;
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(notNullIdPredicate))
    );
  }

  //------------------------------------- Delete methods -------------------------------------------
  @Test
  @WithMockUser(value = "ADMIN")
  void test_delete() {
    assertNotNull(dalService.findById(1));
    dalService.deleteById(1);
    assertThrows(EntityNotFoundException.class, () -> dalService.findById(1));
  }

}
