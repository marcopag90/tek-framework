package com.tek.rest.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Default DTO returned from Rest API call failures.
 *
 * @author MarcoPagan
 */
public class ApiError implements Serializable {

  @JsonProperty("timestamp")
  private final Instant timestamp;
  @JsonProperty("status")
  private HttpStatus status;
  @JsonProperty("code")
  private Integer code;
  @JsonProperty("path")
  private String path;
  @JsonProperty("message")
  private String message;
  @JsonProperty("exceptionMessage")
  private String exceptionMessage;
  @JsonProperty("validationErrors")
  private List<ApiValidationError> validationErrors;

  private ApiError() {
    this.timestamp = Instant.now();
  }

  public ApiError(@NonNull ApiErrorDto dto) {
    this();
    this.path = resolvePath(dto.getRequest());
    this.message = resolveMessage(dto.getMessage());
    this.exceptionMessage = resolveDebugMessage(dto.getEx());
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public Integer getCode() {
    return code;
  }

  public String getPath() {
    return path;
  }

  public String getMessage() {
    return message;
  }

  public String getExceptionMessage() {
    return exceptionMessage;
  }

  public List<ApiValidationError> getValidationErrors() {
    return validationErrors;
  }

  private int resolveCode(HttpStatus status) {
    return status.value();
  }

  private String resolveMessage(String message) {
    if (StringUtils.isNotBlank(message)) {
      return message;
    } else {
      return "Unexpected error";
    }
  }

  private String resolvePath(WebRequest request) {
    if (request == null) {
      return null;
    }
    String pathValue = null;
    if (request instanceof ServletWebRequest servletWebRequest) {
      pathValue = servletWebRequest.getRequest().getRequestURI();
    }
    return pathValue;
  }

  private String resolveDebugMessage(Throwable ex) {
    if (ex == null) {
      return null;
    }
    return ExceptionUtils.getMessage(ex);
  }


  public static class ApiErrorDto {

    private WebRequest request;
    private String message;
    private Throwable ex;

    public WebRequest getRequest() {
      return request;
    }

    public void setRequest(WebRequest request) {
      this.request = request;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public Throwable getEx() {
      return ex;
    }

    public void setEx(Throwable ex) {
      this.ex = ex;
    }
  }

  private void addSubError(ApiValidationError subError) {
    if (validationErrors == null) {
      validationErrors = new ArrayList<>();
    }
    validationErrors.add(subError);
  }

  private void addValidationError(
      String object,
      String field,
      Object rejectedValue,
      String message
  ) {
    addSubError(new ApiValidationError(object, field, rejectedValue, message));
  }

  private void addValidationError(String object, String message) {
    addSubError(new ApiValidationError(object, message));
  }

  private void addValidationError(FieldError fieldError) {
    this.addValidationError(
        fieldError.getObjectName(),
        fieldError.getField(),
        fieldError.getRejectedValue(),
        fieldError.getDefaultMessage()
    );
  }

  public void addValidationErrors(List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addValidationError);
  }

  private void addValidationError(ObjectError objectError) {
    this.addValidationError(
        objectError.getObjectName(),
        objectError.getDefaultMessage()
    );
  }

  public void addValidationError(List<ObjectError> globalErrors) {
    globalErrors.forEach(this::addValidationError);
  }

  /**
   * Utility method to add error of {@link ConstraintViolation}.
   * <p> Usually when a {@link org.springframework.validation.annotation.Validated} validation
   * fails.
   *
   * @param cv the {@link ConstraintViolation}
   */
  private void addValidationError(ConstraintViolation<?> cv) {
    this.addValidationError(
        cv.getRootBeanClass().getSimpleName(),
        ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
        cv.getInvalidValue(),
        cv.getMessage()
    );
  }

  public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
    constraintViolations.forEach(this::addValidationError);
  }
}
