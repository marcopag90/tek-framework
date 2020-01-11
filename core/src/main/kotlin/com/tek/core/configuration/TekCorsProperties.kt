package com.tek.core.configuration

import com.tek.core.util.TekProperty

/**
 * Configuration lookup for [TekCorsFilter].
 *
 * Default configuration is provided if none is found (development purpose only)
 *
 */
class TekCorsProperties {

    var allowedOrigin: TekProperty = "http://localhost:4200"

    var allowedCredentials: TekProperty = "true"

    var allowedMethods: Array<TekProperty> = arrayOf("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")

    var allowedHeaders: Array<TekProperty> =
        arrayOf("x-requested-with", "authorization", "Content-Type", "Authorization", "credential", "X-XSRF-TOKEN")
}

