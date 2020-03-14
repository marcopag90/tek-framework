package com.tek.audit.repository

import com.tek.audit.model.WebAudit
import com.tek.core.repository.TekEntityRepository
import org.springframework.stereotype.Repository

@Repository
interface WebAuditRepository : TekEntityRepository<WebAudit, Long> {
}