package com.tek.rest.shared.exception;


import static com.tek.rest.shared.utils.TekRestSharedUtils.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.tek.rest.shared.exception.TekRestExceptionHandlerTest.TestController;
import com.tek.rest.shared.exception.TekRestExceptionHandlerTest.TestController.Body;
import java.io.InputStream;
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
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

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

  private static final String GENERIC_EXCEPTION = "/handleGenericException";
  private static final String ILLEGAL_ARGUMENT_EXCEPTION = "/handleIllegalArgumentException";
  private static final String ACCESS_DENIED_EXCEPTION = "/handleAccessDeniedException";
  private static final String MISSING_SERVLET_REQUEST_PARAM_EXCEPTION = "/handleMissingServletRequestParameter";
  private static final String MEDIATYPE_NOT_SUPPORTED_EXCEPTION = "/handleHttpMediaTypeNotSupported";
  private static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "/handleMethodArgumentNotValid";
  private static final String CONSTRAINT_VIOLATION_EXCEPTION = "/handleConstraintViolation";
  private static final String ENTITY_NOT_FOUND_EXCEPTION = "/handleEntityNotFound";
  private static final String HTTP_MESSAGE_NOT_READABLE_EXCEPTION = "/handleHttpMessageNotReadable";
  private static final String HTTP_MESSAGE_NOT_WRITABLE_EXCEPTION = "/handleHttpMessageNotWritable";
  private static final String NO_HANDLER_FOUND_EXCEPTION = "/handleNoHandlerFoundException";
  private static final String METHOD_ARG_TYPE_MISMATCH_EXCEPTION = "/handleMethodArgumentTypeMismatch";

  @BeforeAll
  @Test
  void setup() {
    Assertions.assertNotNull(mockMvc);
  }

  @Test
  void test_handle_generic_exception() throws Exception {
    var debugMessage = ExceptionUtils.getMessage(new Exception("error"));
    mockMvc.perform(MockMvcRequestBuilders.get(GENERIC_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
        .andExpect(jsonPath("$.path").value(GENERIC_EXCEPTION))
        .andExpect(jsonPath("$.message").value("error"))
        .andExpect(jsonPath("$.exceptionMessage").value(debugMessage))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void test_handle_illegal_argument_exception() throws Exception {
    var msg = "illegal argument";
    var debugMessage = ExceptionUtils.getMessage(new IllegalArgumentException(msg));
    mockMvc.perform(MockMvcRequestBuilders.get(ILLEGAL_ARGUMENT_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.path").value(ILLEGAL_ARGUMENT_EXCEPTION))
        .andExpect(jsonPath("$.message").value(msg))
        .andExpect(jsonPath("$.exceptionMessage").value(debugMessage))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void test_handle_access_denied_exception() throws Exception {
    var debugMessage = ExceptionUtils.getMessage(new AccessDeniedException("Access denied!"));
    mockMvc.perform(MockMvcRequestBuilders.get(ACCESS_DENIED_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.name()))
        .andExpect(jsonPath("$.path").value(ACCESS_DENIED_EXCEPTION))
        .andExpect(jsonPath("$.message").value("Access denied!"))
        .andExpect(jsonPath("$.exceptionMessage").value(debugMessage))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void test_handle_missing_servlet_parameter() throws Exception {
    var ex = new MissingServletRequestParameterException("name", "string");
    var exMessage = ex.getParameterName() + " parameter is missing";
    var debugMessage = ExceptionUtils.getMessage(ex);
    mockMvc.perform(MockMvcRequestBuilders.get(MISSING_SERVLET_REQUEST_PARAM_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.path").value(MISSING_SERVLET_REQUEST_PARAM_EXCEPTION))
        .andExpect(jsonPath("$.message").value(exMessage))
        .andExpect(jsonPath("$.exceptionMessage").value(debugMessage))
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
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name()))
        .andExpect(jsonPath("$.path").value(MEDIATYPE_NOT_SUPPORTED_EXCEPTION))
        .andExpect(jsonPath("$.message").value(exMessage))
        .andExpect(jsonPath("$.exceptionMessage").value(debugMessage))
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
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.path").value(METHOD_ARGUMENT_NOT_VALID_EXCEPTION))
        .andExpect(jsonPath("$.message").value("Validation error"))
        .andExpect(jsonPath("$.subErrors[0].object").value("body"))
        .andExpect(jsonPath("$.subErrors[0].field").value("name"))
        .andExpect(jsonPath("$.subErrors[0].message").value("must not be blank"))
        .andExpect(jsonPath("$.subErrors[0].rejectedValue").value(""))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void test_handle_constraint_violation() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(CONSTRAINT_VIOLATION_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.path").value(CONSTRAINT_VIOLATION_EXCEPTION))
        .andExpect(jsonPath("$.message").value("Validation error"))
        .andExpect(jsonPath("$.subErrors[0].object").value("Body"))
        .andExpect(jsonPath("$.subErrors[0].field").value("name"))
        .andExpect(jsonPath("$.subErrors[0].message").value("must not be blank"))
        .andExpect(jsonPath("$.subErrors[0].rejectedValue").doesNotExist());
  }

  @Test
  void test_handle_entity_not_found_() throws Exception {
    EntityNotFoundException entityNotFoundException = new EntityNotFoundException(Body.class, 1);
    mockMvc.perform(MockMvcRequestBuilders.get(ENTITY_NOT_FOUND_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
        .andExpect(jsonPath("$.path").value(ENTITY_NOT_FOUND_EXCEPTION))
        .andExpect(jsonPath("$.message").value(entityNotFoundException.getMessage()));
  }

  @Test
  void test_handle_http_message_not_readable() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(HTTP_MESSAGE_NOT_READABLE_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.path").value(HTTP_MESSAGE_NOT_READABLE_EXCEPTION))
        .andExpect(jsonPath("$.message").value("Malformed JSON request"));
  }

  @Test
  void test_handle_http_message_not_writable() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(HTTP_MESSAGE_NOT_WRITABLE_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
        .andExpect(jsonPath("$.path").value(HTTP_MESSAGE_NOT_WRITABLE_EXCEPTION))
        .andExpect(jsonPath("$.message").value("Error writing JSON output"));
  }

  @Test
  void test_handle_no_handler_found_exception() throws Exception {
    NoHandlerFoundException exception =
        new NoHandlerFoundException(
            HttpMethod.GET.name(), NO_HANDLER_FOUND_EXCEPTION,
            new HttpHeaders()
        );
    String exceptionMessage = String.format(
        "Could not find the %s method for URL %s",
        exception.getHttpMethod(),
        exception.getRequestURL()
    );
    mockMvc.perform(MockMvcRequestBuilders.get(NO_HANDLER_FOUND_EXCEPTION))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.path").value(NO_HANDLER_FOUND_EXCEPTION))
        .andExpect(jsonPath("$.message").value(exceptionMessage));
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
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
        .andExpect(jsonPath("$.path").value(METHOD_ARG_TYPE_MISMATCH_EXCEPTION))
        .andExpect(jsonPath("$.message").value(exceptionMessage));
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

    @GetMapping(ILLEGAL_ARGUMENT_EXCEPTION)
    void handleIllegalArgumentException() throws IllegalArgumentException {
      throw new IllegalArgumentException("illegal argument");
    }

    @GetMapping(ACCESS_DENIED_EXCEPTION)
    void handleAccessDeniedException() throws AccessDeniedException {
      throw new AccessDeniedException("Access denied!");
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

    @GetMapping(ENTITY_NOT_FOUND_EXCEPTION)
    void handleEntityNotFoundException() throws EntityNotFoundException {
      throw new EntityNotFoundException(Body.class, 1);
    }

    @GetMapping(HTTP_MESSAGE_NOT_READABLE_EXCEPTION)
    void handleHttpMessageNotReadable() {
      throw new HttpMessageNotReadableException("not readable", new HttpInputMessage() {
        @Override
        public InputStream getBody() {
          return null;
        }

        @Override
        public HttpHeaders getHeaders() {
          return null;
        }
      });
    }

    @GetMapping(HTTP_MESSAGE_NOT_WRITABLE_EXCEPTION)
    void handleHttpMessageNotWritable() {
      throw new HttpMessageNotWritableException("not writable");
    }

    @GetMapping(NO_HANDLER_FOUND_EXCEPTION)
    void handleNoHandlerFoundException() throws NoHandlerFoundException {
      throw new NoHandlerFoundException(
          HttpMethod.GET.name(), NO_HANDLER_FOUND_EXCEPTION, new HttpHeaders()
      );
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
