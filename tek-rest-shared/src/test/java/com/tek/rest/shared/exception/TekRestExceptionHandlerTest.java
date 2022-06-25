package com.tek.rest.shared.exception;


import static com.tek.rest.shared.utils.TekRestSharedUtils.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.tek.rest.shared.exception.TekRestExceptionHandlerTest.TestController;
import com.tek.rest.shared.exception.TekRestExceptionHandlerTest.TestController.Body;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        TekRestExceptionHandler.class,
        TestController.class
    }
)
@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class TekRestExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  private static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "/handleMethodArgumentNotValid";
  private static final String ENTITY_NOT_FOUND_EXCEPTION = "/handleEntityNotFound";
  private static final String METHOD_ARG_TYPE_MISMATCH_EXCEPTION = "/handleMethodArgumentTypeMismatch";

  @BeforeAll
  @Test
  void setup() {
    Assertions.assertNotNull(mockMvc);
  }


  @SuppressWarnings("squid:S2699")
  @Test
  void test_handle_method_argument_not_valid() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post(METHOD_ARGUMENT_NOT_VALID_EXCEPTION)
            .content(asJsonString(Body.builder().name("").build()))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.path").value(METHOD_ARGUMENT_NOT_VALID_EXCEPTION))
        .andExpect(jsonPath("$.message").value("Validation error"))
        .andExpect(jsonPath("$.validationErrors[0].object").value("body"))
        .andExpect(jsonPath("$.validationErrors[0].field").value("name"))
        .andExpect(jsonPath("$.validationErrors[0].message").value("must not be blank"))
        .andExpect(jsonPath("$.validationErrors[0].rejectedValue").value(""))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void test_handle_entity_not_found_() throws Exception {
    EntityNotFoundException entityNotFoundException = new EntityNotFoundException(Body.class, 1);
    mockMvc.perform(MockMvcRequestBuilders.get(ENTITY_NOT_FOUND_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.path").value(ENTITY_NOT_FOUND_EXCEPTION))
        .andExpect(jsonPath("$.message").value(entityNotFoundException.getMessage()));
  }

  @Test
  void test_handle_method_argument_type_mismatch_exception() throws Exception {
    MethodArgumentTypeMismatchException exception = fakeMethodArgumentTypeMismatchException();
    Class<?> requiredType = exception.getRequiredType();
    String exceptionMessage = String.format(
        "The parameter '%s' of value '%s' could not be converted to type '%s'",
        exception.getName(),
        exception.getValue(),
        (requiredType != null) ? requiredType.getSimpleName() : null
    );
    mockMvc.perform(MockMvcRequestBuilders.get(METHOD_ARG_TYPE_MISMATCH_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.path").value(METHOD_ARG_TYPE_MISMATCH_EXCEPTION))
        .andExpect(jsonPath("$.message").value(exceptionMessage));
  }

  @RestController
  @SuppressWarnings("unused")
  static class TestController {

    @Autowired
    private Validator validator;

    @PostMapping(METHOD_ARGUMENT_NOT_VALID_EXCEPTION)
    void handleMethodArgumentNotValid(@Valid @RequestBody Body body) {
    }

    @GetMapping(ENTITY_NOT_FOUND_EXCEPTION)
    void handleEntityNotFoundException() throws EntityNotFoundException {
      throw new EntityNotFoundException(Body.class, 1);
    }

    @GetMapping(METHOD_ARG_TYPE_MISMATCH_EXCEPTION)
    void handleMethodArgumentTypeMismatch() throws NoSuchMethodException {
      throw fakeMethodArgumentTypeMismatchException();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Validated
    static class Body {

      @NotBlank
      private String name;
    }
  }

  @SneakyThrows
  private static MethodArgumentTypeMismatchException fakeMethodArgumentTypeMismatchException() {
    Object value = 1;
    Class<?> requiredType = String.class;
    String name = "getName";
    MethodParameter param = new MethodParameter(Body.class.getMethod(name), -1);
    Throwable cause = new NullPointerException();
    return new MethodArgumentTypeMismatchException(value, requiredType, name, param, cause);
  }

}
