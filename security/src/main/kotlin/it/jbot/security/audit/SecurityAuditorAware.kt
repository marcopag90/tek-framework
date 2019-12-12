package it.jbot.security.audit

import it.jbot.security.service.AuthService
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
    private val authService: AuthService
) : AuditorAware<Any> {

    override fun getCurrentAuditor(): Optional<Any> =
        Optional.ofNullable(authService.getCurrentUser()?.id)
}



