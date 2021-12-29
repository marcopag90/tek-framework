package com.tek.rest.shared.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Default DTO returned from Rest API call failures.
 *
 * @author MarcoPagan
 */
@Getter
@ToString
@JsonTypeName("apiError")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class ApiError implements Serializable {

  private final Instant timestamp;
  private HttpStatus status;
  private Integer code;
  private String path;
  private String message;
  private String exceptionMessage;
  private List<ApiSubError> subErrors;

  private ApiError() {
    this.timestamp = Instant.now();
  }

  public ApiError(ApiErrorDto dto) {
    this();
    Objects.requireNonNull(dto.getStatus(), "status cannot be null!");
    this.status = dto.getStatus();
    this.code = resolveCode(dto.getStatus());
    this.path = resolvePath(dto.getRequest());
    this.message = resolveMessage(dto.getMessage());
    this.exceptionMessage = resolveDebugMessage(dto.getEx());
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

  @Getter
  @Setter
  public static class ApiErrorDto {

    private HttpStatus status;
    private WebRequest request;
    private String message;
    private Throwable ex;
  }

  private void addSubError(ApiSubError subError) {
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
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
