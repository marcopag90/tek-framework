package com.tek.core.constants;

/**
 * Beans exposed by the module.
 *
 * @author MarcoPagan
 */
public final class TekCoreBeanNames {

  private TekCoreBeanNames() {
  }

  public static final String TEK_CORE_MODULE_CONFIGURATION = "com.tek.core.config.TekCoreModuleConfiguration";
  public static final String TEK_CORE_WEB_MVC_CONFIGURATION = "com.tek.core.config.web.TekCoreWebMvc";
  public static final String TEK_CORE_WEB_MVC_TIMESTAMP = "com.tek.core.config.web.TekCoreWebMvc.timestampSimpleDateFormat";
  public static final String TEK_CORE_WEB_MVC_FILE_TIMESTAMP = "com.tek.core.config.web.TekCoreWebMvc.fileTimestampFormat";
  public static final String TEK_CORE_PROP_PLACEHOLDER_CONFIGURATION = "com.tek.core.config.TekCoreModuleConfiguration.placeholderConfigurer";
  public static final String TEK_CORE_LOCALE_CHANGE_INTERCEPTOR = "com.tek.core.config.i18n.localeChangeInterceptor";
  public static final String TEK_CORE_BIN_DIR_CONFIGURATION = "com.tek.core.config.directory.TekBinaryDirConfiguration";
  public static final String TEK_CORE_BIN_SCHEDULER_CONFIGURATION = "com.tek.core.config.scheduler.TekBinDirSchedulerConfiguration";
  public static final String TEK_CORE_BIN_DIR = "com.tek.core.config.directory.TekBinaryDirConfiguration.binDirectory";
  public static final String TEK_CORE_BIN_FILE_SERVICE = "com.tek.core.service.TekBinDirFileService";
  public static final String TEK_CORE_TMP_DIR_CONFIGURATION = "com.tek.core.config.directory.TekTmpDirConfiguration";
  public static final String TEK_CORE_TMP_SCHEDULER_CONFIGURATION = "com.tek.core.config.scheduler.TekTmpDirSchedulerConfiguration";
  public static final String TEK_CORE_TMP_DIR = "com.tek.core.config.directory.TekTmpDirConfiguration.tmpDirectory";
  public static final String TEK_CORE_TMP_FILE_SERVICE = "com.tek.core.service.TekTmpDirFileService";
  public static final String TEK_CORE_ACCEPT_HEADER_CONFIGURATION = "com.tek.core.i18n.TekAcceptHeaderLocaleConfiguration";
  public static final String TEK_CORE_COOKIE_LOCALE_CONFIGURATION = "com.tek.core.i18n.TekCookieLocaleConfiguration";
  public static final String TEK_CORE_COOKIE_LOCALE_RESOLVER = "com.tek.core.i18n.TekCookieLocaleConfiguration.localeResolver";
  public static final String TEK_CORE_SESSION_LOCALE_CONFIGURATION = "com.tek.core.i18n.TekSessionLocaleConfiguration";
  public static final String TEK_CORE_SESSION_LOCALE_RESOLVER = "com.tek.core.i18n.TekSessionLocaleConfiguration.localeResolver";
  public static final String TEK_CORE_RESOURCE_BUNDLE_CONFIGURATION = "com.tek.core.i18n.TekResourceBundleConfiguration";
  public static final String TEK_CORE_RESOURCE_BUNDLE = "com.tek.core.i18n.TekResourceBundleConfiguration.tekMessageSource";
  public static final String TEK_CORE_SWAGGER_CONFIGURATION = "com.tek.core.config.swagger.TekSwaggerConfiguration";
  public static final String TEK_CORE_SWAGGER_API = "com.tek.core.config.swagger.TekSwaggerConfiguration.api";
  public static final String TEK_CORE_SWAGGER_API_INFO = "com.tek.core.config.swagger.TekSwaggerConfiguration.apiInfo";
  public static final String TEK_CORE_MAIL_SERVICE = "com.tek.core.service.MailService";
}
