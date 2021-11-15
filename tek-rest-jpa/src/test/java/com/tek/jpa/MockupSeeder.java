package com.tek.jpa;

import com.tek.jpa.domain.Author;
import com.tek.jpa.repository.AuthorRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MockupSeeder implements CommandLineRunner {

  @Autowired
  private AuthorRepository authorRepository;

  @Override
  public void run(String... args) throws Exception {
    var tolkien = Author.builder()
        .name("John Ronald Reuel")
        .surname("Tolkien")
        .birthDate(LocalDate.of(1892, Month.JANUARY, 3))
        .build();
    var calvino = Author.builder()
        .name("Italo")
        .surname("Calvino")
        .birthDate(LocalDate.of(1923, Month.OCTOBER, 15))
        .build();
    var stephenKing = Author.builder()
        .name("Stephen")
        .surname("King")
        .birthDate(LocalDate.of(1947, Month.SEPTEMBER, 21))
        .build();
    authorRepository.saveAll(List.of(tolkien, calvino, stephenKing));
  }
}
