package com.tek.core

import com.tek.core.configuration.TekCorsProperties
import com.tek.core.data.TekRunnerProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Suppress("UNUSED")
@Configuration
@ConfigurationProperties(prefix = "tek")
class TekProperties {
    var cors: TekCorsProperties? = null
    var runner: TekRunnerProperties? = null
}





