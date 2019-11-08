package it.jbot.security.audit

import org.javers.spring.auditable.SpringSecurityAuthorProvider

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