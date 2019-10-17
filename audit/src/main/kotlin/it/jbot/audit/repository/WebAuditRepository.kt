package it.jbot.audit.repository

import it.jbot.audit.model.WebAudit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WebAuditRepository : JpaRepository<WebAudit, Long> {
}