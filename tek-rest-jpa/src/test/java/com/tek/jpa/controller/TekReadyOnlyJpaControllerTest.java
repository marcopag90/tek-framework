package com.tek.jpa.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tek.jpa.TekRestJpaApplication;
import com.tek.jpa.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = TekRestJpaApplication.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application.yml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class TekReadyOnlyJpaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private AuthorRepository authorRepository;

  // ----------------------------------- Authorization Tests ---------------------------------------

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_readAll_with_user_view() throws Exception {
    var count = (double) authorRepository.count();
    var page = 0;
    var size = 2;
    var totalPages = Math.ceil(count / size);
    var parameters = String.format("page=%s&size=%s", page, size);
    var url = String.format("%s?%s", AuthorReadyOnlyCrudController.PATH, parameters);
    var result = mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.totalElements").value(count))
        .andExpect(jsonPath("$.totalPages").value(totalPages))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    // content.get(index) returns null if the JsonNode is not present
    Assertions.assertAll(
        () -> assertNull(content.get(0).get("id")),
        () -> assertNull(content.get(1).get("id"))
    );
  }

  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_readAll_with_developer_view_authorized() throws Exception {
    var count = (double) authorRepository.count();
    var page = 0;
    var size = 2;
    var totalPages = Math.ceil(count / size);
    var parameters = String.format("page=%s&size=%s", page, size);
    var url = String.format("%s?%s", AuthorReadyOnlyCrudController.PATH, parameters);
    var result = mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.totalElements").value(count))
        .andExpect(jsonPath("$.totalPages").value(totalPages))
        .andDo(print())
        .andReturn();
    var content = mapper.readTree(result.getResponse().getContentAsByteArray()).get("content");
    // content.get(index) returns null if the JsonNode is not present
    Assertions.assertAll(
        () -> assertFalse(content.get(0).get("id").isMissingNode()),
        () -> assertFalse(content.get(1).get("id").isMissingNode())
    );

  }

  @Test
  @WithMockUser(
      value = "USER",
      authorities = {"USER", "AUTHOR_READ"}
  )
  void test_readOne_with_user_view_authorized() throws Exception {
    var url = AuthorReadyOnlyCrudController.PATH + "/1";
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
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_readOne_with_developer_view_authorized() throws Exception {
    var url = AuthorReadyOnlyCrudController.PATH + "/1";
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").exists())
        .andExpect(jsonPath("$.surname").exists())
        .andExpect(jsonPath("$.birthDate").exists())
        .andExpect(jsonPath("$.deathDate").exists());
  }

  // ----------------------------------- Pagination tests -----------------------------------------
  @Test
  @WithMockUser(
      value = "ADMIN",
      authorities = {"DEVELOPER", "AUTHOR_READ"}
  )
  void test_pagination() {

  }

  // ----------------------------------- Operators tests -------------------------------------------

}
