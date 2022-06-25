package com.tek.rest.shared.exception;

import com.tek.rest.shared.dto.ApiError;
import com.tek.rest.shared.dto.ApiError.ApiErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Handler for errors raised from API REST calls.
 *
 * @author MarcoPagan
 */
@Order
@RestControllerAdvice
public class TekRestExceptionHandler {

  private final Logger log = LoggerFactory.getLogger(TekRestExceptionHandler.class);

  /**
   * Handle {@link MethodArgumentNotValidException}.
   * <p>Triggered when an object fails @Valid validation.
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull WebRequest request
  ) {
    log.error(ex.getMessage(), ex);
    final var dto = new ApiErrorDto();
    dto.setRequest(request);
    dto.setMessage("Validation error");
    final var apiError = new ApiError(dto);
    apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
    apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
    return apiError;
  }

  /**
   * Handle {@link MethodArgumentTypeMismatchException}
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handleMethodArgumentTypeMismatch(
      @NonNull MethodArgumentTypeMismatchException ex,
      @NonNull WebRequest request
  ) {
    log.error(ex.getMessage() != null ? ex.getMessage() : "", ex);
    final var dto = new ApiErrorDto();
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
    return new ApiError(dto);
  }

  /**
   * Handle {@link EntityNotFoundException}.
   *
   * @return the {@link ApiError} object
   */
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiError handleEntityNotFound(
      @NonNull EntityNotFoundException ex,
      @NonNull WebRequest request
  ) {
    log.error(ex.getMessage() != null ? ex.getMessage() : "", ex);
    final var dto = new ApiErrorDto();
    dto.setRequest(request);
    dto.setMessage(ex.getMessage());
    dto.setEx(ex);
    return new ApiError(dto);
  }
}
