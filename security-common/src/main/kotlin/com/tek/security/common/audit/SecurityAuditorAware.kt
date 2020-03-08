package com.tek.security.common.audit

import com.tek.security.common.service.TekAuthService
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

/**
 * Component to provide a qualifier for @CreatedBy and @LastModifiedBy.
 *
 * In default implementation it provides UserDetails.id
 */
@Component
class SecurityAuditorAware(
    private val tekAuthService: TekAuthService
) : AuditorAware<Any> {

    override fun getCurrentAuditor(): Optional<Any> =
        Optional.ofNullable(tekAuthService.getCurrentUser()?.id)
}



