package com.tek.core.constants;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_ACCEPT_HEADER_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_BIN_DIR;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_BIN_DIR_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_BIN_FILE_SERVICE;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_BIN_SCHEDULER_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_COOKIE_LOCALE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_COOKIE_LOCALE_RESOLVER;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_LOCALE_CHANGE_INTERCEPTOR;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_MAIL_SERVICE;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_MODULE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_PROP_PLACEHOLDER_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_RESOURCE_BUNDLE;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_RESOURCE_BUNDLE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SESSION_LOCALE_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SESSION_LOCALE_RESOLVER;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SWAGGER_API;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SWAGGER_API_INFO;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_SWAGGER_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_TMP_DIR;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_TMP_DIR_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_TMP_FILE_SERVICE;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_TMP_SCHEDULER_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_WEB_MVC_CONFIGURATION;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_WEB_MVC_FILE_TIMESTAMP;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_WEB_MVC_TIMESTAMP;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TekCoreBeanNamesTest {

  @Test
  @SuppressWarnings("squid:S5961")
  void testConstantValues() {
    Assertions.assertAll(
        () -> assertEquals(
            "com.tek.core.config.TekCoreModuleConfiguration",
            TEK_CORE_MODULE_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.config.web.TekCoreWebMvc",
            TEK_CORE_WEB_MVC_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.config.web.TekCoreWebMvc.timestampSimpleDateFormat",
            TEK_CORE_WEB_MVC_TIMESTAMP
        ),
        () -> assertEquals(
            "com.tek.core.config.web.TekCoreWebMvc.fileTimestampFormat",
            TEK_CORE_WEB_MVC_FILE_TIMESTAMP
        ),
        () -> assertEquals(
            "com.tek.core.config.TekCoreModuleConfiguration.placeholderConfigurer",
            TEK_CORE_PROP_PLACEHOLDER_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.config.i18n.localeChangeInterceptor",
            TEK_CORE_LOCALE_CHANGE_INTERCEPTOR
        ),
        () -> assertEquals(
            "com.tek.core.config.directory.TekBinaryDirConfiguration",
            TEK_CORE_BIN_DIR_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.service.TekBinDirFileService",
            TEK_CORE_BIN_FILE_SERVICE
        ),
        () -> assertEquals(
            "com.tek.core.config.directory.TekBinaryDirConfiguration.binDirectory",
            TEK_CORE_BIN_DIR
        ),
        () -> assertEquals(
            "com.tek.core.config.directory.TekTmpDirConfiguration",
            TEK_CORE_TMP_DIR_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.config.scheduler.TekBinDirSchedulerConfiguration",
            TEK_CORE_BIN_SCHEDULER_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.config.directory.TekTmpDirConfiguration.tmpDirectory",
            TEK_CORE_TMP_DIR
        ),
        () -> assertEquals(
            "com.tek.core.config.scheduler.TekTmpDirSchedulerConfiguration",
            TEK_CORE_TMP_SCHEDULER_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.service.TekTmpDirFileService",
            TEK_CORE_TMP_FILE_SERVICE
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekAcceptHeaderLocaleConfiguration",
            TEK_CORE_ACCEPT_HEADER_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekCookieLocaleConfiguration",
            TEK_CORE_COOKIE_LOCALE_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekCookieLocaleConfiguration.localeResolver",
            TEK_CORE_COOKIE_LOCALE_RESOLVER
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekSessionLocaleConfiguration",
            TEK_CORE_SESSION_LOCALE_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekSessionLocaleConfiguration.localeResolver",
            TEK_CORE_SESSION_LOCALE_RESOLVER
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekResourceBundleConfiguration",
            TEK_CORE_RESOURCE_BUNDLE_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.i18n.TekResourceBundleConfiguration.tekMessageSource",
            TEK_CORE_RESOURCE_BUNDLE
        ),
        () -> assertEquals(
            "com.tek.core.config.swagger.TekSwaggerConfiguration",
            TEK_CORE_SWAGGER_CONFIGURATION
        ),
        () -> assertEquals(
            "com.tek.core.config.swagger.TekSwaggerConfiguration.api",
            TEK_CORE_SWAGGER_API
        ),
        () -> assertEquals(
            "com.tek.core.config.swagger.TekSwaggerConfiguration.apiInfo",
            TEK_CORE_SWAGGER_API_INFO
        ),
        () -> assertEquals(
            "com.tek.core.service.MailService",
            TEK_CORE_MAIL_SERVICE
        )
    );
  }
}
