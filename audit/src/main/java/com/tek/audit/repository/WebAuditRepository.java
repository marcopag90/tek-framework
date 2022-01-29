package com.tek.audit.repository;

import com.tek.audit.model.WebAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WebAuditRepository extends JpaRepository<WebAudit, Long>,
    JpaSpecificationExecutor<WebAudit> {

  @Override
  Page<WebAudit> findAll(Specification<WebAudit> spec, Pageable pageable);
}
