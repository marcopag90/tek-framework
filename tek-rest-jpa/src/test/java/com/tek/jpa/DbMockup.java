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
public class DbMockup implements CommandLineRunner {

  @Autowired
  private AuthorRepository authorRepository;

  @Override
  public void run(String... args) throws Exception {
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
    authorRepository.saveAll(List.of(calvino, stephenKing));
  }
}
