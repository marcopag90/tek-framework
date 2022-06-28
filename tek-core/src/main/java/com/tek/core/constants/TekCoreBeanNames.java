package com.tek.core.constants;

/**
 * Beans exposed by the module.
 *
 * @author MarcoPagan
 */
public final class TekCoreBeanNames {

  private TekCoreBeanNames() {
  }

  public static final String TEK_CORE_LOCALE_CHANGE_INTERCEPTOR = "com.tek.core.config.i18n.localeChangeInterceptor";
  public static final String TEK_CORE_ACCEPT_HEADER_CONFIGURATION = "com.tek.core.i18n.TekAcceptHeaderLocaleConfiguration";
  public static final String TEK_CORE_COOKIE_LOCALE_CONFIGURATION = "com.tek.core.i18n.TekCookieLocaleConfiguration";
  public static final String TEK_CORE_COOKIE_LOCALE_RESOLVER = "com.tek.core.i18n.TekCookieLocaleConfiguration.localeResolver";
  public static final String TEK_CORE_SESSION_LOCALE_CONFIGURATION = "com.tek.core.i18n.TekSessionLocaleConfiguration";
  public static final String TEK_CORE_RESOURCE_BUNDLE_CONFIGURATION = "com.tek.core.i18n.TekResourceBundleConfiguration";
  public static final String TEK_CORE_RESOURCE_BUNDLE = "com.tek.core.i18n.TekResourceBundleConfiguration.tekMessageSource";
}
