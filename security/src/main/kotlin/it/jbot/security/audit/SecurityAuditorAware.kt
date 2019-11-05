package it.jbot.security.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import it.jbot.security.JBotUserDetails
import it.jbot.security.service.JBotAuthService
import org.javers.core.metamodel.annotation.DiffIgnore
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.stereotype.Component
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * Component to provide a qualifier for @CreatedBy and @LastModifiedBy.
 *
 * In default implementation it provides UserDetails.id
 */
@Component
class SecurityAuditorAware(
    private val authService: JBotAuthService
) : AuditorAware<Any> {
    
    override fun getCurrentAuditor(): Optional<Any> =
        Optional.ofNullable(authService.getCurrentUser()?.id)
}



