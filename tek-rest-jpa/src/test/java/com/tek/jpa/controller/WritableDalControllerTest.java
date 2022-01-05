package com.tek.jpa.controller;

import static com.tek.rest.shared.utils.TekRestSharedUtils.asJsonString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.controller.mock.AuthorWritableDalController;
import com.tek.jpa.domain.Author;
import com.tek.jpa.repository.mock.AuthorRepository;
import com.tek.jpa.repository.mock.BeerRepository;
import com.tek.jpa.repository.mock.BookRepository;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
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
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@Transactional
class WritableDalControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;
  @Autowired private AuthorRepository authorRepository;
  @Autowired private BookRepository bookRepository;
  @Autowired private BeerRepository beerRepository;

  @Autowired
  private ApplicationContext context;

  @BeforeAll
  @Test
  void test_setup() {
    Assertions.assertAll(
        () -> assertNotNull(mockMvc),
        () -> assertNotNull(mapper),
        () -> assertNotNull(authorRepository),
        () -> assertNotNull(bookRepository),
        () -> assertNotNull(beerRepository)
    );
  }

  // -------------------------------------- Authorizations -----------------------------------------

  @Test
  @WithMockUser(value = "USER")
  void test_create_forbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(AuthorWritableDalController.PATH))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  @Test
  @WithMockUser(value = "USER")
  void test_readAll_forbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(AuthorWritableDalController.PATH))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(value = "USER")
  void test_readOne_forbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(AuthorWritableDalController.PATH + "/1"))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(value = "USER")
  void test_delete_forbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete(AuthorWritableDalController.PATH + "/1"))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(print());
  }

  // ------------------------------------ Create methods -------------------------------------------

  @Test
  @WithMockUser(value = "USER", authorities = {"USER", "AUTHOR_CREATE"})
  void test_create_with_user_view() throws Exception {
    var birthDate = LocalDate.of(1990, Month.MAY, 23);
    var author = Author.builder()
        .name("Marco")
        .surname("Pagan")
        .birthDate(birthDate)
        .build();
    mockMvc.perform(MockMvcRequestBuilders.post(AuthorWritableDalController.PATH)
            .content(asJsonString(author))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").doesNotExist())
        .andExpect(jsonPath("$.name").isNotEmpty())
        .andExpect(jsonPath("$.surname").isNotEmpty())
        .andExpect(jsonPath("$.birthDate").isNotEmpty())
        .andExpect(jsonPath("$.deathDate").doesNotExist())
        .andExpect(jsonPath("$.pseudonym").doesNotExist())
        .andExpect(jsonPath("$.absolute").doesNotExist())
        .andExpect(jsonPath("$.books").isEmpty())
        .andExpect(jsonPath("$.ratings").isEmpty())
        .andDo(print());
  }

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"DEVELOPER", "AUTHOR_CREATE"})
  void test_create_with_developer_view() throws Exception {
    var birthDate = LocalDate.of(1990, Month.MAY, 23);
    var author = Author.builder()
        .name("Marca")
        .surname("Pagan")
        .birthDate(birthDate)
        .build();
    mockMvc.perform(MockMvcRequestBuilders.post(AuthorWritableDalController.PATH)
            .content(asJsonString(author))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").isNotEmpty())
        .andExpect(jsonPath("$.surname").isNotEmpty())
        .andExpect(jsonPath("$.birthDate").isNotEmpty())
        .andExpect(jsonPath("$.deathDate").doesNotExist())
        .andExpect(jsonPath("$.pseudonym").doesNotExist())
        .andExpect(jsonPath("$.absolute").doesNotExist())
        .andExpect(jsonPath("$.books").isEmpty())
        .andExpect(jsonPath("$.ratings").isEmpty())
        .andDo(print());
  }

  //------------------------------------- Update methods -------------------------------------------


  // ------------------------------------ Delete methods -------------------------------------------

  @Test
  @WithMockUser(value = "ADMIN", authorities = {"AUTHOR_DELETE"})
  void test_delete() throws Exception {
    Assertions.assertFalse(authorRepository.findById(1).isEmpty());
    mockMvc.perform(MockMvcRequestBuilders.delete(AuthorWritableDalController.PATH + "/1"))
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        .andDo(print());
    Assertions.assertTrue(authorRepository.findById(1).isEmpty());
  }
}
