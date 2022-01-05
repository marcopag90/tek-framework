package com.tek.jpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Comparators;
import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.domain.Author;
import com.tek.jpa.repository.mock.AuthorWritableDalRepository;
import com.turkraft.springfilter.FilterBuilder;
import com.turkraft.springfilter.boot.FilterSpecification;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = TekRestJpaApplication.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@Transactional
public class WritableDalRepositoryTest {

  @Autowired
  private AuthorWritableDalRepository repository;

  @BeforeAll
  @Test
  void test_setup() {
    Assertions.assertAll(
        () -> assertNotNull(repository)
    );
  }

  @Test
  void test_create() {
    final var marco = Author.builder()
        .name("Marco")
        .surname("Pagan")
        .birthDate(LocalDate.of(1990,5,23))
        .pseudonym("Pag")
        .build();
    final var savedMarco = repository.create(marco);
    Assertions.assertAll(
        () -> assertNotNull(savedMarco),
        () -> assertNotNull(savedMarco.getId()),
        () -> assertEquals(marco.getName(), savedMarco.getName()),
        () -> assertEquals(marco.getSurname(), savedMarco.getSurname()),
        () -> assertEquals(marco.getBirthDate(), savedMarco.getBirthDate()),
        () -> assertEquals(marco.getPseudonym(), savedMarco.getPseudonym())
    );
  }

  @Test
  void test_findOne() {
    final var spec = new FilterSpecification<Author>(FilterBuilder.equal("id", 1));
    Optional<Author> optAuthor = repository.findOne(spec);
    Assertions.assertAll(
        () -> assertTrue(optAuthor.isPresent()),
        () -> assertEquals(1, optAuthor.get().getId())
    );
  }

  @Test
  void test_findAll() {
    final var pageable = PageRequest.of(0, 20, Direction.DESC, "id");
    final var result = repository.findAll(null, pageable);
    var idComparatorDesc = Comparator.comparing(Author::getId).reversed();
    Assertions.assertAll(
        () -> assertFalse(result.isEmpty()),
        () -> assertTrue(Comparators.isInOrder(result, idComparatorDesc))
    );
  }

  @Test
  void test_update() {
    final var optionalAuthor = repository.findOne(new FilterSpecification<>("id: 1"));
    Assertions.assertTrue(optionalAuthor.isPresent());
    final var author = optionalAuthor.get();
    author.setName("Luca");
    final var updatedAuthor = repository.update(author);
    Assertions.assertAll(
        () -> assertNotNull(updatedAuthor),
        () -> assertEquals("Luca", updatedAuthor.getName())
    );
  }

  @Test
  void test_deleteById() {
    final var optionalAuthor = repository.findOne(new FilterSpecification<>("id: 2"));
    Assertions.assertTrue(optionalAuthor.isPresent());
    repository.deleteById(2);
    final var deletedAuthor = repository.findOne(new FilterSpecification<>("id: 2"));
    Assertions.assertTrue(deletedAuthor.isEmpty());
  }
}
