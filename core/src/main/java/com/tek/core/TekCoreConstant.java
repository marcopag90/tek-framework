package com.tek.core;

/**
 * Tek Core Constants
 *
 * @author MarcoPagan
 */
public interface TekCoreConstant {

    /**
     * Default packages to scan by Spring
     */
    String TEK_PACKAGES_TO_SCAN = "com.tek";

    /**
     * Tek Core Module Configuration name
     */
    String TEK_CORE_CONFIGURATION = "TekCoreConfiguration";

    /**
     * Classpath location for i18n message bundle
     */
    String TEK_CORE_MESSAGE_BUNDLE = "classpath:/i18n/core_messages";

    /**
     * Default name of i18n message bundle
     */
    String TEK_CORE_MESSAGE_SOURCE = "com.tek.core.messageSource";

    /**
     * Prefix of the .yaml/.properties for Tek Core Module configuration.
     */
    String TEK_CORE = "tek.core";

    /**
     * Locale change API path
     */
    String TEK_LOCALE_PATH = "/locale";

    /**
     * Date format API path
     */
    String TEK_DATE_PATH = "/dateformat";

    /**
     * Name of the .yaml property for Swagger API info
     */
    String TEK_SWAGGER_API_INFO = "tek.swagger.api.info";

    /**
     * Name of the git.properties file
     */
    String GIT_PROPERTIES = "git.properties";
}
