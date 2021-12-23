package com.tek.jpa;

import com.github.javafaker.Faker;
import com.tek.jpa.domain.Author;
import com.tek.jpa.domain.Beer;
import com.tek.jpa.domain.Book;
import com.tek.jpa.domain.Project;
import com.tek.jpa.domain.Store;
import com.tek.jpa.domain.Store.Id;
import com.tek.jpa.repository.AuthorRepository;
import com.tek.jpa.repository.BeerRepository;
import com.tek.jpa.repository.BookRepository;
import com.tek.jpa.repository.ProjectRepository;
import com.tek.jpa.repository.StoreRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseMockup implements CommandLineRunner {

  private final AuthorRepository authorRepository;
  private final BookRepository bookRepository;
  private final BeerRepository beerRepository;
  private final StoreRepository storeRepository;
  private final ProjectRepository projectRepository;

  @Override
  public void run(String... args) {
    createAuthors();
    createBrewery();
    createStores();
    createProjects();
  }


  private void createAuthors() {
    var calvino = Author.builder()
        .name("Italo")
        .surname("Calvino")
        .birthDate(LocalDate.of(1923, Month.OCTOBER, 15))
        .deathDate(LocalDate.of(1973, Month.SEPTEMBER, 2))
        .build();
    var stephenKing = Author.builder()
        .name("Stephen")
        .surname("King")
        .ratings(List.of(5, 10))
        .birthDate(LocalDate.of(1947, Month.SEPTEMBER, 21))
        .build();
    var umbertoEco = Author.builder()
        .name("Umberto")
        .surname("Eco")
        .absolute(-1)
        .ratings(List.of(1, 2, 3, 4, 5))
        .pseudonym("   ")
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
    Book leCittaInvisibili = Book.builder()
        .author(calvino)
        .title("Le città invisibili")
        .build();
    Book ilBaroneRampante = Book.builder()
        .author(calvino)
        .title("Il Barone Rampante")
        .build();
    Book uomoInFuga = Book.builder()
        .author(stephenKing)
        .title("Uomo in fuga")
        .build();
    bookRepository.saveAll(
        List.of(
            leCittaInvisibili,
            ilBaroneRampante,
            uomoInFuga
        )
    );
  }

  private void createBrewery() {
    var brewery = new ArrayList<Beer>();
    for (int i = 0; i < 100; i++) {
      Beer beer = new Beer(Faker.instance().beer().name());
      brewery.add(beer);
    }
    beerRepository.saveAll(brewery);
  }

  private void createStores() {
    storeRepository.save(Store.builder().
        id(new Id("Mediaworld", 1))
        .build()
    );
  }

  private void createProjects() {
    projectRepository.save(Project.builder()
        .name("TekFramework")
        .type("Java")
        .build()
    );
  }
}
