package com.tek.core.constants;

/**
 * Tek Core Constants
 *
 * @author MarcoPagan
 */
public class TekCoreConstants {

  private TekCoreConstants() {
  }

  /**
   * Default packages to scan by Spring
   */
  public static final String TEK_CORE_PACKAGES_TO_SCAN = "com.tek.core";

  /**
   * Classpath location for i18n message bundle
   */
  public static final String TEK_CORE_MESSAGE_BUNDLE = "classpath:/i18n/core_messages";

  /**
   * Prefix of the .yaml/.properties for Tek Core Module configuration.
   */
  public static final String TEK_CORE_PREFIX = "tek.core";

  /**
   * Locale change API path
   */
  @SuppressWarnings("squid:S1075")
  public static final String TEK_LOCALE_PATH = "/locale";

  /**
   * Date format API path
   */
  @SuppressWarnings("squid:S1075")
  public static final String TEK_DATE_PATH = "/dateformat";

  /**
   * Name of the git.properties file
   */
  public static final String GIT_PROPERTIES = "git.properties";
}
