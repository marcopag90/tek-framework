package com.tek.core.service.impl;

import static com.tek.core.constants.TekCoreBeanConstants.TEK_CORE_MESSAGE_SOURCE_BEAN;

import com.tek.core.config.i18n.TekCoreMessageSourceConfiguration;
import com.tek.core.service.TekRestMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;


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
  @Qualifier(TEK_CORE_MESSAGE_SOURCE_BEAN)
  private final MessageSource messageSource;

  public String ok() {
    return messageSource.getMessage(
        TekCoreMessageSourceConfiguration.Message.SERVICE_OK,
        null,
        LocaleContextHolder.getLocale()
    );
  }

  public String ko() {
    return messageSource.getMessage(
        TekCoreMessageSourceConfiguration.Message.SERVICE_KO,
        null,
        LocaleContextHolder.getLocale()
    );
  }
}