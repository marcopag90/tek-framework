package com.tek.core.controller.i18n;

import com.tek.core.config.i18n.TekCoreMessageSource;
import com.tek.core.service.TekRestMessage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tek.core.TekCoreConstants.TEK_CORE_MESSAGE_SOURCE;
import static com.tek.core.TekCoreConstants.TEK_LOCALE_PATH;

/**
 * API to test the behaviour of the Spring {@link org.springframework.web.servlet.i18n.LocaleChangeInterceptor}.
 *
 * @author MarcoPagan
 */
@RestController
@RequestMapping(
    path = TEK_LOCALE_PATH,
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
class TekLocaleController {

  @NonNull
  @Qualifier(TEK_CORE_MESSAGE_SOURCE)
  private final MessageSource messageSource;

  @NonNull
  private final TekRestMessage tekRestMessage;

  @PostMapping
  @ApiOperation(value = "Allows to change the server locale")
  @ApiImplicitParam(
      paramType = "form",
      dataType = "string",
      dataTypeClass = String.class,
      name = "locale",
      value = "Value of the locale to set, provided in the format BCP 47",
      allowableValues = "it, en",
      required = true
  )
  public ResponseEntity<String> setLocale() {
    val message = messageSource.getMessage(
        TekCoreMessageSource.Message.LOCALE_LANG, null, LocaleContextHolder.getLocale()
    );
    return ResponseEntity.ok(message);
  }
}
