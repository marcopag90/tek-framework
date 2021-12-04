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
        .deathDate(LocalDate.of(1973, Month.SEPTEMBER, 2))
        .build();
    var stephenKing = Author.builder()
        .name("Stephen")
        .surname("King")
        .birthDate(LocalDate.of(1947, Month.SEPTEMBER, 21))
        .build();
    var umbertoEco = Author.builder()
        .name("Umberto")
        .surname("Eco")
        .birthDate(LocalDate.of(1932, Month.JANUARY, 5))
        .deathDate(LocalDate.of(2016, Month.FEBRUARY, 19))
        .build();
    authorRepository.saveAll(
        List.of(
            calvino,
            stephenKing,
            umbertoEco
        )
    );
  }
}
