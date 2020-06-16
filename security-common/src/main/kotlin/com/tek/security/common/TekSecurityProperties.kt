package com.tek.security.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.convert.DurationUnit
import org.springframework.context.annotation.Configuration
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties(prefix = TEK_SECURITY_MODULE)
class TekSecurityProperties {

    var registerProfile: String by Delegates.notNull()

    @DurationUnit(ChronoUnit.DAYS)
    var passwordExpiration: Duration? = null

    @DurationUnit(ChronoUnit.DAYS)
    var accountExpiration: Duration? = null
}
