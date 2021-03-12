package com.tek.core;

/**
 * Configurable constants properties to inject into spring.profiles.active property
 * (application.yaml or application.properties)
 * <p>
 * Example given:
 * <p>
 * - application.properties -> spring.profiles.active=dev
 * <p>
 * - application yaml -> spring.profiles.active:dev
 *
 * @author MarcoPagan
 */
public class TekProfile {

  private TekProfile() {
  }

  /**
   * Profile that MUST be used only for development environment
   **/
  public static final String DEVELOPMENT = "dev";

  /**
   * Profile that MUST be used for test environment
   **/
  public static final String TEST = "test";

  /**
   * Profile that MUST be used for application deployment environment
   **/
  public static final String PRODUCTION = "prod";
}
