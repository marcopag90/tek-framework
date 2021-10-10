package com.tek.core.exception;

import com.tek.core.properties.TekCoreProperties;
import com.tek.core.service.TekErrorService;
import com.tek.core.service.TekMailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Tek Core Exception Handler for all REST calls.
 * <p>
 * This service checks if a mail with errors must be sent via {@link
 * org.springframework.mail.javamail.JavaMailSender}
 *
 * @author MarcoPagan
 */
//TODO fine tune of errors
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class TekExceptionHandler extends ResponseEntityExceptionHandler {

  @NonNull private final TekErrorService errorService;
  @NonNull private final TekMailService mailService;
  @NonNull private final TekCoreProperties coreProperties;

  @ExceptionHandler(value = Exception.class)
  @Order
  public ResponseEntity<Object> handleGenericException(
      Exception ex, WebRequest request
  ) {
    log.error(ExceptionUtils.getStackTrace(ex));
    sendErrorAsMail(request, ex);
    return handleExceptionInternal(
        ex,
        errorService.createErrorResponse(request),
        new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        request
    );
  }

  /**
   * Utility method to check if we have to produce an error mail with the throwable {@link
   * Exception} as attachment.
   */
  private void sendErrorAsMail(WebRequest request, Exception ex) {
    if (coreProperties.getMailConfiguration().isSendErrors()) {
      mailService.sendRequestExceptionMessage((ServletWebRequest) request, ex);
    } else {
      log.warn("Property tek.core.mail.sendErrors is false, skipping mail sending!");
    }
  }
}
