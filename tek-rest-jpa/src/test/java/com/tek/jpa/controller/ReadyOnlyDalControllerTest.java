package com.tek.jpa.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Comparators;
import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.controller.mock.AuthorReadyOnlyDalController;
import com.tek.jpa.controller.mock.BeerReadOnlyDalController;
import com.tek.jpa.domain.Author;
import com.tek.jpa.domain.Beer;
import com.tek.jpa.repository.AuthorRepository;
import com.tek.jpa.repository.BeerRepository;
import com.turkraft.springfilter.FilterBuilder;
import com.turkraft.springfilter.parser.Filter;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = TekRestJpaApplication.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Transactional
class ReadyOnlyDalControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;
  @Autowired private AuthorRepository authorRepository;
  @Autowired private BeerRepository beerRepository;

  // -------------------------------------- Authorizations -----------------------------------------

  @Test
  @WithMockUser(value = "USER")
  void test_readAll_forbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(AuthorReadyOnlyDalController.PATH))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(value = "USER")
  void test_readOne_forbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(AuthorReadyOnlyDalController.PATH + "/1"))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(print());
  }

  // ------------------------------------- Read methods --------------------------------------------

  @Test
  @WithMockUser(value = "USER", authorities = {"USER", "AUTHOR_READ"})
  void test_readAll_with_user_view() throws Exception {
    var result = mockMvc.perform(MockMvcRequestBuilders.get(AuthorReadyOnlyDalController.PATH))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    assertFalse(authors.isEmpty());
    authors.forEach(a -> assertNull(a.getId()));
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_readAll_with_developer_view_authorized() throws Exception {
    var result = mockMvc.perform(MockMvcRequestBuilders.get(AuthorReadyOnlyDalController.PATH))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    assertFalse(authors.isEmpty());
    authors.forEach(a -> assertNotNull(a.getId()));
  }

  @Test
  @WithMockUser(value = "USER", authorities = {"USER", "AUTHOR_READ"})
  void test_readOne_with_user_view_authorized() throws Exception {
    var url = AuthorReadyOnlyDalController.PATH + "/1";
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").doesNotExist())
        .andExpect(jsonPath("$.name").exists())
        .andExpect(jsonPath("$.surname").exists())
        .andExpect(jsonPath("$.birthDate").exists())
        .andExpect(jsonPath("$.deathDate").exists())
        .andDo(print());
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_readOne_with_developer_view_authorized() throws Exception {
    var url = AuthorReadyOnlyDalController.PATH + "/1";
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").exists())
        .andExpect(jsonPath("$.surname").exists())
        .andExpect(jsonPath("$.birthDate").exists())
        .andExpect(jsonPath("$.deathDate").exists());
  }

  // ----------------------------------- Pagination tests ------------------------------------------

  @Test
  @WithMockUser(value = "ADMIN")
  void test_pagination() throws Exception {
    var count = (double) beerRepository.count();
    var page = 2;
    var size = 20;
    var sort = "id,desc";
    var totalPages = Math.ceil(count / size);
    var pagination = String.format("page=%s&size=%s&sort=%s", page, size, sort);
    var url = String.format("%s?%s", BeerReadOnlyDalController.PATH, pagination);
    var result = mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.totalElements").value(count))
        .andExpect(jsonPath("$.totalPages").value(totalPages))
        .andExpect(jsonPath("$.size").value(size))
        .andExpect(jsonPath("$.number").value(page))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var brewery = mapper.readValue(content.toString(), new TypeReference<List<Beer>>() {
    });
    var idComparatorDesc = Comparator.comparing(Beer::getId).reversed();
    Assertions.assertAll(
        () -> assertFalse(brewery.isEmpty()),
        () -> assertTrue(Comparators.isInOrder(brewery, idComparatorDesc))
    );
  }

  // ------------------------------------ Operators tests ------------------------------------------

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_and_predicate() throws Exception {
    var filter = FilterBuilder.and(
        FilterBuilder.equal("name", "Italo"),
        FilterBuilder.equal("surname", "Calvino")
    );
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> andPredicate =
        a -> "Italo".equals(a.getName()) && "Calvino".equals(a.getSurname());
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(andPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_or_predicate() throws Exception {
    var filter = FilterBuilder.or(
        FilterBuilder.equal("name", "Italo"),
        FilterBuilder.equal("name", "Stephen")
    );
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> orPredicate =
        a -> "Italo".equals(a.getName()) || "Stephen".equals(a.getName());
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(orPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_not_predicate() throws Exception {
    var filter = FilterBuilder.not(FilterBuilder.equal("name", "Italo"));
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> notPredicate = a -> !"Italo".equals(a.getName());
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(notPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_like_ends_with() throws Exception {
    var filter = FilterBuilder.like("name", "*lo");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> likePredicate = a -> a.getName().toLowerCase().endsWith("lo");
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(likePredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_like_starts_with() throws Exception {
    var filter = FilterBuilder.like("name", "ita*");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> likePredicate = a -> a.getName().toLowerCase().startsWith("ita");
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(likePredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_like_contains() throws Exception {
    var filter = FilterBuilder.like("name", "*al*");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> likePredicate = a -> a.getName().toLowerCase().contains("al");
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(likePredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_equals() throws Exception {
    var filter = FilterBuilder.equal("name", "Stephen");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> equalsPredicate = a -> a.getName().equals("Stephen");
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(equalsPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_not_equals() throws Exception {
    var filter = FilterBuilder.notEqual("name", "Stephen");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> equalsPredicate = a -> !a.getName().equals("Stephen");
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(equalsPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_greater_than() throws Exception {
    var birthDate = LocalDate.of(1923, Month.OCTOBER, 15);
    var filter = FilterBuilder.greaterThan("birthDate", birthDate);
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    Predicate<Author> greaterThanPredicate = a -> a.getBirthDate().isAfter(birthDate);
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(greaterThanPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_greater_than_or_equal() throws Exception {
    var birthDate = LocalDate.of(1923, Month.OCTOBER, 15);
    var filter = FilterBuilder.greaterThanOrEqual("birthDate", birthDate);
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    Predicate<Author> lowerThanPredicate = a -> a.getBirthDate().isBefore(birthDate);
    Predicate<Author> orEqualPredicate = a -> a.getBirthDate().equals(birthDate);
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().noneMatch(lowerThanPredicate)),
        () -> assertTrue(authors.stream().anyMatch(orEqualPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_lower_than() throws Exception {
    var deathDate = LocalDate.of(2016, Month.FEBRUARY, 19);
    var filter = FilterBuilder.lessThan("deathDate", deathDate);
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    Predicate<Author> lessThanPredicate = a -> a.getDeathDate().isBefore(deathDate);
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(lessThanPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_lower_than_or_equal() throws Exception {
    var deathDate = LocalDate.of(2016, Month.FEBRUARY, 19);
    var filter = FilterBuilder.lessThanOrEqual("deathDate", deathDate);
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    Predicate<Author> greaterThanPredicate = a -> a.getDeathDate().isAfter(deathDate);
    Predicate<Author> orEqualPredicate = a -> a.getDeathDate().equals(deathDate);
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().noneMatch(greaterThanPredicate)),
        () -> assertTrue(authors.stream().anyMatch(orEqualPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_is_null() throws Exception {
    var filter = FilterBuilder.isNull("deathDate");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    Predicate<Author> isNullPredicate = a -> a.getDeathDate() == null;
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(isNullPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_is_not_null() throws Exception {
    var filter = FilterBuilder.isNotNull("deathDate");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    Predicate<Author> isNotNullPredicate = a -> a.getDeathDate() != null;
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(isNotNullPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_is_empty() throws Exception {
    var filter = FilterBuilder.isEmpty("books");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> isEmptyPredicate = a -> a.getBooks().isEmpty();
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(isEmptyPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_is_not_empty() throws Exception {
    var filter = FilterBuilder.isNotEmpty("books");
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> isNotEmptyPredicate = a -> !a.getBooks().isEmpty();
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(isNotEmptyPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_in() throws Exception {
    var filter = FilterBuilder.in(
        "name",
        List.of(FilterBuilder.input("Italo"), FilterBuilder.input("Stephen"))
    );
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> inPredicate =
        a -> a.getName().equals("Stephen") || a.getName().equals("Italo");
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(inPredicate))
    );
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_READ"})
  void test_exists() throws Exception {
    var filter = FilterBuilder.exists(
        FilterBuilder.like("books.title", "*Barone*")
    );
    var result = mockMvc.perform(MockMvcRequestBuilders.get(authorFilterRequest(filter)))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    var authors = mapper.readValue(content.toString(), new TypeReference<List<Author>>() {
    });
    Predicate<Author> existsPredicate =
        a -> a.getBooks().stream().anyMatch(b -> b.getTitle().contains("Barone"));
    Assertions.assertAll(
        () -> assertFalse(authors.isEmpty()),
        () -> assertTrue(authors.stream().allMatch(existsPredicate))
    );
  }

  private String authorFilterRequest(Filter filter) {
    return String.format("%s?q=%s", AuthorReadyOnlyDalController.PATH, filter.generate());
  }
}
