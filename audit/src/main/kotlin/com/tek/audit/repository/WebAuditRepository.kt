package com.tek.audit.repository

import com.tek.audit.model.WebAudit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface WebAuditRepository : JpaRepository<WebAudit, Long>, QuerydslPredicateExecutor<WebAudit>