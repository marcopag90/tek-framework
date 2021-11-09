package com.tek.rest.shared.exception;


import static com.tek.rest.shared.TekRestSharedUtils.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.tek.rest.shared.config.TekJacksonConfig;
import com.tek.rest.shared.exception.TekRestExceptionHandlerTest.TestController;
import com.tek.rest.shared.exception.TekRestExceptionHandlerTest.TestController.Body;
import java.util.HashSet;
import java.util.List;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        TekRestExceptionHandler.class,
        TekJacksonConfig.class,
        TestController.class
    }
)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class TekRestExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  private static final String GENERIC_EXCEPTION = "/handleGenericException";
  private static final String MISSING_SERVLET_REQUEST_PARAM_EXCEPTION = "/handleMissingServletRequestParameter";
  private static final String MEDIATYPE_NOT_SUPPORTED_EXCEPTION = "/handleHttpMediaTypeNotSupported";
  private static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "/handleMethodArgumentNotValid";
  private static final String CONSTRAINT_VIOLATION_EXCEPTION = "/constraintViolationException";

  @Test
  void test_handle_generic_exception() throws Exception {
    var debugMessage = ExceptionUtils.getMessage(new Exception("error"));
    mockMvc.perform(MockMvcRequestBuilders.get(GENERIC_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(jsonPath("$.apiError.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.apiError.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
        .andExpect(jsonPath("$.apiError.path").value(GENERIC_EXCEPTION))
        .andExpect(jsonPath("$.apiError.message").value("error"))
        .andExpect(jsonPath("$.apiError.exceptionMessage").value(debugMessage))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void test_handle_missing_servlet_parameter() throws Exception {
    var ex = new MissingServletRequestParameterException("name", "string");
    var exMessage = ex.getParameterName() + " parameter is missing";
    var debugMessage = ExceptionUtils.getMessage(ex);
    mockMvc.perform(MockMvcRequestBuilders.get(MISSING_SERVLET_REQUEST_PARAM_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.apiError.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.apiError.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.apiError.path").value(MISSING_SERVLET_REQUEST_PARAM_EXCEPTION))
        .andExpect(jsonPath("$.apiError.message").value(exMessage))
        .andExpect(jsonPath("$.apiError.exceptionMessage").value(debugMessage))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void test_handle_mediatype_not_supported() throws Exception {
    var ex = new HttpMediaTypeNotSupportedException(
        MediaType.APPLICATION_XML,
        List.of(MediaType.APPLICATION_JSON)
    );
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
    var exMessage = builder.substring(0, builder.length() - 2);
    var debugMessage = ExceptionUtils.getMessage(ex);
    mockMvc.perform(MockMvcRequestBuilders.get(MEDIATYPE_NOT_SUPPORTED_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andExpect(jsonPath("$.apiError.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.apiError.status").value(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name()))
        .andExpect(jsonPath("$.apiError.path").value(MEDIATYPE_NOT_SUPPORTED_EXCEPTION))
        .andExpect(jsonPath("$.apiError.message").value(exMessage))
        .andExpect(jsonPath("$.apiError.exceptionMessage").value(debugMessage))
        .andDo(MockMvcResultHandlers.print());
  }

  @SuppressWarnings("squid:S2699")
  @Test
  void test_handle_method_argument_not_valid() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post(METHOD_ARGUMENT_NOT_VALID_EXCEPTION)
            .content(asJsonString(Body.builder().name("").build()))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andExpect(jsonPath("$.apiError.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.apiError.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.apiError.path").value(METHOD_ARGUMENT_NOT_VALID_EXCEPTION))
        .andExpect(jsonPath("$.apiError.message").value("Validation error"))
        .andExpect(jsonPath("$.apiError.subErrors[0].object").value("body"))
        .andExpect(jsonPath("$.apiError.subErrors[0].field").value("name"))
        .andExpect(jsonPath("$.apiError.subErrors[0].message").value("must not be blank"))
        .andExpect(jsonPath("$.apiError.subErrors[0].rejectedValue").value(""))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void test_handle_constraint_violation_exception() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(CONSTRAINT_VIOLATION_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andExpect(jsonPath("$.apiError.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.apiError.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.apiError.path").value(CONSTRAINT_VIOLATION_EXCEPTION))
        .andExpect(jsonPath("$.apiError.message").value("Validation error"))
        .andExpect(jsonPath("$.apiError.subErrors[0].object").value("Body"))
        .andExpect(jsonPath("$.apiError.subErrors[0].field").value("name"))
        .andExpect(jsonPath("$.apiError.subErrors[0].message").value("must not be blank"))
        .andExpect(jsonPath("$.apiError.subErrors[0].rejectedValue").doesNotExist());


  }

  @RestController
  @SuppressWarnings("unused")
  static class TestController {

    @Autowired
    private Validator validator;

    @GetMapping(GENERIC_EXCEPTION)
    void handleGenericException() throws Exception {
      throw new Exception("error");
    }

    @GetMapping(MISSING_SERVLET_REQUEST_PARAM_EXCEPTION)
    void handleMissingServletRequestParameter() throws MissingServletRequestParameterException {
      throw new MissingServletRequestParameterException("name", "string");
    }

    @GetMapping(MEDIATYPE_NOT_SUPPORTED_EXCEPTION)
    void handleHttpMediaTypeNotSupported() throws HttpMediaTypeNotSupportedException {
      throw new HttpMediaTypeNotSupportedException(
          MediaType.APPLICATION_XML,
          List.of(MediaType.APPLICATION_JSON)
      );
    }

    @PostMapping(METHOD_ARGUMENT_NOT_VALID_EXCEPTION)
    void handleMethodArgumentNotValid(@Valid @RequestBody Body body) {
    }

    @GetMapping(CONSTRAINT_VIOLATION_EXCEPTION)
    void handleConstraintViolationException() {
      throw new ConstraintViolationException(
          new HashSet<>(validator.validate(new Body()))
      );
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


}
