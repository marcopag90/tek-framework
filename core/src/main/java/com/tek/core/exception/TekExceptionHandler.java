package com.tek.core.exception;

import com.tek.core.TekCoreProperties;
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
 * This service checks if a mail with errors must be sent via
 * {@link org.springframework.mail.javamail.JavaMailSender}
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
        RuntimeException ex, WebRequest request
    ) {
        log.error(ExceptionUtils.getStackTrace(ex));
        sendExceptionAsMail(request, ex);
        return handleExceptionInternal(
            ex,
            errorService.createErrorResponse(request),
            new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
    }

    /**
     * Utility method to check if we have to produce an error mail
     * with the throwable {@link RuntimeException} as attachment.
     *
     * @author MarcoPagan
     */
    private void sendExceptionAsMail(WebRequest request, RuntimeException ex) {
        if (coreProperties.getMail().isSendErrors()) {
            if (coreProperties.getMail().isRealDelivery()) {
                mailService.sendExceptionMessage((ServletWebRequest) request, ex);
            } else {
                log.warn("Parameter sendError is active but realDelivery is false. Skipping mail sending!");
            }
        }
    }
}
