package com.tek.core;

/**
 * Configurable constants properties to inject into spring.profiles.active property
 * (application.yaml or application.properties)
 * <p>
 * Example given:
 * <p>
 * - application.properties -> spring.profiles.active=dev
 * <p>
 * - application yaml -> spring:profiles.active:dev
 *
 * @author MarcoPagan
 */
public interface TekProfile {

    /**
     * Profile that MUST be used only for development environment
     **/
    String DEVELOPMENT = "dev";

    /**
     * Profile that MUST be used for test environment
     **/
    String TEST = "test";

    /**
     * Profile that MUST be used for application deployment environment
     **/
    String PRODUCTION = "prod";
}
