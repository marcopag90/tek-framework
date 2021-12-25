package com.tek.jpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Comparators;
import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.domain.Author;
import com.tek.jpa.repository.mock.AuthorReadOnlyDalRepository;
import com.turkraft.springfilter.FilterBuilder;
import com.turkraft.springfilter.boot.FilterSpecification;
import java.util.Comparator;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
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
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@Transactional
class ReadOnlyDalRepositoryTest {

  @Autowired
  private AuthorReadOnlyDalRepository repository;

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
}
