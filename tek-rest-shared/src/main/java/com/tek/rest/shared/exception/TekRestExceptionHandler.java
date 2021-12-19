package com.tek.rest.shared.exception;

import com.tek.rest.shared.TekRestSharedAutoConfig;
import com.tek.rest.shared.dto.ApiError;
import com.tek.rest.shared.dto.ApiError.ApiErrorDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handler for errors raised from API REST calls.
 *
 * @author MarcoPagan
 */
@ConditionalOnClass(TekRestSharedAutoConfig.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class TekRestExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private ApplicationContext context;

  /**
   * Handle generic {@link Exception}.
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(value = Exception.class)
  @Order
  public ResponseEntity<Object> handleGenericException(
      @NonNull Exception ex,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    dto.setRequest(request);
    dto.setEx(ex);
    if (StringUtils.isNotEmpty(ex.getMessage())) {
      dto.setMessage(ex.getMessage());
    }
    return buildResponseEntity(new ApiError(dto));
  }

  //TODO handleIllegalArgumentException
  public ResponseEntity<Object> handleIllegalArgumentException() {
    throw new NotImplementedException();
  }

  /**
   * Handle {@link AccessDeniedException}
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(value = AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(
      @NonNull AccessDeniedException ex,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.FORBIDDEN);
    dto.setRequest(request);
    dto.setEx(ex);
    if (StringUtils.isNotEmpty(ex.getMessage())) {
      dto.setMessage(ex.getMessage());
    }
    return buildResponseEntity(new ApiError(dto));
  }

  /**
   * Handle {@link MissingServletRequestParameterException}.
   * <p>Triggered when a 'required' request parameter is missing.
   *
   * @return the {@link ApiError} object
   */
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      @NonNull MissingServletRequestParameterException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.BAD_REQUEST);
    dto.setRequest(request);
    dto.setMessage(ex.getParameterName() + " parameter is missing");
    dto.setEx(ex);
    return buildResponseEntity(new ApiError(dto));
  }

  /**
   * Handle {@link HttpMediaTypeNotSupportedException}.
   * <p>Triggered when JSON is invalid as well.
   *
   * @return the {@link ApiError} object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      @NonNull HttpMediaTypeNotSupportedException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request
  ) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    dto.setRequest(request);
    dto.setMessage(builder.substring(0, builder.length() - 2));
    dto.setEx(ex);
    return buildResponseEntity(new ApiError(dto));
  }

  /**
   * Handle {@link MethodArgumentNotValidException}.
   * <p>Triggered when an object fails @Valid validation.
   *
   * @return the {@link ApiError} object
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.BAD_REQUEST);
    dto.setRequest(request);
    dto.setMessage("Validation error");
    final var apiError = new ApiError(dto);
    apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
    apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles javax.validation.{@link javax.validation.ConstraintViolationException}.
   * <p>Triggered when @{@link org.springframework.validation.annotation.Validated} fails.
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(javax.validation.ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolation(
      @NonNull javax.validation.ConstraintViolationException ex,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setRequest(request);
    dto.setStatus(HttpStatus.BAD_REQUEST);
    dto.setMessage("Validation error");
    dto.setEx(ex);
    final var apiError = new ApiError(dto);
    apiError.addValidationErrors(ex.getConstraintViolations());
    return buildResponseEntity(apiError);
  }

  /**
   * Handles {@link EntityNotFoundException}.
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(
      @NonNull EntityNotFoundException ex,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.NOT_FOUND);
    dto.setRequest(request);
    dto.setMessage(ex.getMessage());
    dto.setEx(ex);
    return buildResponseEntity(new ApiError(dto));
  }

  /**
   * Handle {@link HttpMessageNotReadableException}.
   * <p>Triggered when request JSON is malformed.
   *
   * @return the {@link ApiError} object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      @NonNull HttpMessageNotReadableException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request
  ) {
    final var srvReq = (ServletWebRequest) request;
    log.debug("{} to {}", srvReq.getHttpMethod(), srvReq.getRequest().getServletPath());
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.BAD_REQUEST);
    dto.setRequest(request);
    dto.setMessage("Malformed JSON request");
    dto.setEx(ex);
    return buildResponseEntity(new ApiError(dto));
  }

  /**
   * Handle {@link HttpMessageNotWritableException}.
   *
   * @return the {@link ApiError} object
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(
      @NonNull HttpMessageNotWritableException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    dto.setRequest(request);
    dto.setMessage("Error writing JSON output");
    dto.setEx(ex);
    return buildResponseEntity(new ApiError(dto));
  }

  /**
   * Handle {@link NoHandlerFoundException}.
   *
   * @return the {@link ApiError} object
   */
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      @NonNull NoHandlerFoundException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.BAD_REQUEST);
    dto.setRequest(request);
    dto.setMessage(String.format(
        "Could not find the %s method for URL %s",
        ex.getHttpMethod(),
        ex.getRequestURL())
    );
    dto.setEx(ex);
    return buildResponseEntity(new ApiError(dto));
  }

  /**
   * Handle {@link javax.persistence.EntityNotFoundException}.
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(
      @NonNull javax.persistence.EntityNotFoundException ex,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.NOT_FOUND);
    dto.setRequest(request);
    dto.setEx(ex);
    return buildResponseEntity(new ApiError(dto));
  }

  /**
   * Handle {@link MethodArgumentTypeMismatchException}
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      @NonNull MethodArgumentTypeMismatchException ex,
      @NonNull WebRequest request
  ) {
    final var dto = new ApiErrorDto();
    dto.setStatus(HttpStatus.BAD_REQUEST);
    dto.setRequest(request);
    dto.setEx(ex);
    Class<?> requiredType = ex.getRequiredType();
    dto.setMessage(
        String.format(
            "The parameter '%s' of value '%s' could not be converted to type '%s'",
            ex.getName(),
            ex.getValue(),
            (requiredType != null) ? requiredType.getSimpleName() : null
        )
    );
    return buildResponseEntity(new ApiError(dto));
  }

  @SneakyThrows
  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
