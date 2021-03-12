package com.tek.core.config.i18n;

import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

import static com.tek.core.TekCoreConstant.TEK_CORE_MESSAGE_BUNDLE;
import static com.tek.core.TekCoreConstant.TEK_CORE_MESSAGE_SOURCE;

@Configuration
public class TekCoreMessageSource {

  @Bean(name = TEK_CORE_MESSAGE_SOURCE)
  public MessageSource getMessageSource() {
    val messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename(TEK_CORE_MESSAGE_BUNDLE);
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    return messageSource;
  }

  public static class Message {

    private Message() {
    }

    public static final String LOCALE_LANG = "locale.lang";

    public static final String ERROR_EMPTY_FIELD = "error.empty.field";
    public static final String ERROR_NOTFOUND_RESOURCE = "error.notFound.resource";
    public static final String ERROR_UNKNOWN_PROPERTY = "error.unknown.property";

    public static final String SERVICE_OK = "service.executed";
    public static final String SERVICE_KO = "error.service.execution";
  }
}