package com.tek.core.service.impl;

import com.tek.core.config.i18n.TekCoreMessageSource;
import com.tek.core.service.TekRestMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import static com.tek.core.TekCoreConstant.TEK_CORE_MESSAGE_SOURCE;

/**
 * Service to provide default messages for all the API responses, based on the session locale.
 *
 * @author MarcoPagan
 */
@Service
@ConditionalOnSingleCandidate(TekRestMessage.class)
@RequiredArgsConstructor
public class TekRestMessageServiceImpl implements TekRestMessage {

  @NonNull
  @Qualifier(TEK_CORE_MESSAGE_SOURCE)
  private final MessageSource messageSource;

  public String ok() {
    return messageSource.getMessage(
        TekCoreMessageSource.Message.SERVICE_OK,
        null,
        LocaleContextHolder.getLocale()
    );
  }

  public String ko() {
    return messageSource.getMessage(
        TekCoreMessageSource.Message.SERVICE_KO,
        null,
        LocaleContextHolder.getLocale()
    );
  }
}
