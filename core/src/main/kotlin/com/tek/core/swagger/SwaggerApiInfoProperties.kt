package com.tek.core.swagger

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "swagger.api.info")
class SwaggerApiInfoProperties {

    var title: String = "Tek Framework"
    var description: String = "Tek Framework API Service"
    var termOfServiceUrl: String = ""

    var contactName: String = ""
    var contactUrl: String = ""
    var contactEmail: String = ""

    var license: String? = null
    var licenseUrl: String? = null
}