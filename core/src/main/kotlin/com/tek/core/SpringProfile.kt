package com.tek.core

/**
 * Configurable constants properties to inject into spring.profiles.active property (application.yaml or application.properties)
 *
 * Example given:
 * - application.properties -> spring.profiles.active=dev
 * - application yaml -> spring:profiles.active:dev
 */
object SpringProfile {

    /** Profile that _MUST_ be used only for development environment**/
    const val DEVELOPMENT = "dev"

    /** Profile that _MUST_ be used for application deployment environment**/
    const val PRODUCTION = "prod"

    /** Profile that _SHOULD_ be used for test environment**/
    const val TEST = "test"
}



