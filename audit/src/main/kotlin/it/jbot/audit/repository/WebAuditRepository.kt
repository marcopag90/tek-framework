package it.jbot.audit.repository

import it.jbot.audit.model.WebAudit
import it.jbot.core.repository.JBotRepository
import org.springframework.stereotype.Repository

@Repository
interface WebAuditRepository : JBotRepository<WebAudit, Long> {
}