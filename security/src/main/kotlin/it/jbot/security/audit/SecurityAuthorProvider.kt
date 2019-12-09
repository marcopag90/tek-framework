package it.jbot.security.audit

import org.javers.spring.auditable.SpringSecurityAuthorProvider

//TODO change to default and add username id as property
class SecurityAuthorProvider(
    private val securityAuditorAware: SecurityAuditorAware
) : SpringSecurityAuthorProvider() {
    
    override fun provide(): String {
        
        return if (securityAuditorAware.currentAuditor.isPresent)
            securityAuditorAware.currentAuditor.get().toString()
        else
            "unauthenticated"
    }
}