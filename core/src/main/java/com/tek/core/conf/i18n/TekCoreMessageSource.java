package com.tek.core.conf.i18n;

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
        ReloadableResourceBundleMessageSource messageSource
            = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename(TEK_CORE_MESSAGE_BUNDLE);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

        return messageSource;
    }

    public static class Message {
        public static String LOCALE_LANG = "locale.lang";

        public static String ERROR_EMPTY_FIELD = "error.empty.field";
        public static String ERROR_NOTFOUND_RESOURCE = "error.notFound.resource";
        public static String ERROR_UNKNOWN_PROPERTY = "error.unknown.property";

        public static String SERVICE_OK = "service.executed";
        public static String SERVICE_KO = "error.service.execution";
    }

}
