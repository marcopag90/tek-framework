package com.tek.audit.repository

import com.tek.audit.model.WebAudit
import com.tek.core.repository.TekRepository
import org.springframework.stereotype.Repository

@Repository
interface WebAuditRepository : TekRepository<WebAudit, Long> {
}