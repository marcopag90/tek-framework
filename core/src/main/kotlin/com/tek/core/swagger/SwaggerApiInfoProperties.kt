package com.tek.core.swagger

import com.tek.core.TEK_SWAGGER_API_INFO
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = TEK_SWAGGER_API_INFO)
class SwaggerApiInfoProperties {

    var title: String = "Tek Framework"
    var description: String = "Tek Framework API Service"
    var termOfServiceUrl: String? = null

    var contactName: String? = null
    var contactUrl: String? = null
    var contactEmail: String? = null

    var license: String? = null
    var licenseUrl: String? = null

    override fun toString(): String = """
        SwaggerApiInfoProperties:
        tek.swagger.api.info:
            title: $title,
            description: $description,
            termOfServiceUrl: $termOfServiceUrl,
            contactName: $contactName,
            contactUrl: $contactUrl,
            contactEmail: $contactEmail,
            license: $license,
            licenseUrl: $licenseUrl
    """.trimIndent()
}